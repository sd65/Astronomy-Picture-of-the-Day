package com.doignon.sylvain.apodnasa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

public class Presentation extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.presentation, container, false);

        ImageView image = (ImageView) rootView.findViewById(R.id.image);
        TextView title = (TextView) rootView.findViewById(R.id.title);
        TextView description = (TextView) rootView.findViewById(R.id.description);

        final Date date = (Date) getArguments().getSerializable("date");

        Boolean needExtraMargin = getArguments().getBoolean("needExtraMargin");
        if(needExtraMargin) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) description.getLayoutParams();
            params.bottomMargin = 370;
            description.setLayoutParams(params);
        }
        Boolean noTouchImage = getArguments().getBoolean("noTouchImage");
        if(!noTouchImage) {
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), Maximize_image.class);
                    i.putExtra("date", date);
                    startActivity(i);
                }
            });
        }

        MyAsyncTask a = new MyAsyncTask();
        a.setContext(getContext());
        a.setImage(image);
        a.setTitle(title);
        a.setDescription(description);
        a.setDate(date);
        a.execute();

        return rootView;

    }
}