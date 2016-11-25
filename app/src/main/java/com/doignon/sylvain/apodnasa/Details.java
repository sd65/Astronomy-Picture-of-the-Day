package com.doignon.sylvain.apodnasa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.Date;

public class Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Fragment newFragment = new Presentation();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Bundle args = new Bundle();
        Date date = (Date) getIntent().getSerializableExtra("date");
        args.putBoolean("noTouchImage", true);
        args.putSerializable("date", date);
        newFragment.setArguments(args);

        transaction.replace(R.id.base,  newFragment);
        transaction.disallowAddToBackStack();

        // Commit the transaction
        transaction.commit();
    }
}
