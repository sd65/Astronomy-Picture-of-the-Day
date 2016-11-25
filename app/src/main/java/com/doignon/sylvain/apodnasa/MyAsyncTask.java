package com.doignon.sylvain.apodnasa;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

class MyAsyncTask extends AsyncTask<Object, String, JSONObject> {

    private static DateFormat dateFormat;


    private Date date;
    private ImageView image;
    private TextView description;
    private TextView title;
    private Boolean forMaximize;
    private Context context;
    private String defaultUrl;

    public void setImage(ImageView image) {
        this.image = image;
    }

    void setContext(Context context) {
        this.context = context;
    }

    void setDate(Date date) {
        this.date = date;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    void setDescription(TextView description) {
        this.description = description;
    }

    void setForMaximize(boolean centerInside) {
        this.forMaximize = centerInside;
    }

    MyAsyncTask() {
        forMaximize = false;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    }

    @Override
    protected JSONObject doInBackground(Object... args) {
        defaultUrl = context.getString(R.string.default_image);

        String nasa_api_url = context.getString(R.string.nasa_api_url);
        String nasa_api_key = context.getString(R.string.nasa_api_key);

        String sdate = dateFormat.format(date);

        Uri builtUri = Uri.parse(nasa_api_url)
                .buildUpon()
                .appendQueryParameter("api_key", nasa_api_key)
                .appendQueryParameter("hd", "true")
                .appendQueryParameter("date", sdate)
                .build();

        URL url;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            return new JSONObject();
        }

        AtomicReference<HttpURLConnection> urlConnection = new AtomicReference<>();
        try {
            urlConnection.set((HttpURLConnection) url.openConnection());
        } catch (IOException e) {
            return new JSONObject();
        }

        JSONObject json = null;
        try {
            InputStream in = new BufferedInputStream(urlConnection.get().getInputStream());
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            json = new JSONObject(responseStrBuilder.toString());
        } catch (Exception e) {
            return new JSONObject();
        } finally {
            urlConnection.get().disconnect();
        }
        // We have the json, notify progress
        return json;
    }

    @Override
    protected void onPostExecute(JSONObject json) {

        String jurl = "";
        String jtitle = "";
        String jdescription = "";
        String jsdate = "";

        if (json.has("url")) {
            try {
                String media_type = (String) json.get("media_type");
                if (media_type.equals("image"))
                    jurl = (String) json.get("url");
                else
                    jurl = defaultUrl;
                jtitle = (String) json.get("title");
                jdescription = (String) json.get("explanation");
                jsdate = (String) json.get("date");
            } catch (JSONException e) {
                this.cancel(true);
            }

            if (forMaximize)
                Picasso.with(context).load(jurl).resize(4096, 4096).onlyScaleDown().centerInside()
                        .placeholder(R.drawable.wait).error(R.drawable.error).into(image);
            else
                Picasso.with(context).load(jurl).fit().centerCrop().placeholder(R.drawable.wait)
                        .error(R.drawable.error).into(image);

            ImageInfo i = new ImageInfo(jtitle, jurl, date, jsdate);
            image.setTag(i);

            if (title != null)
                title.setText(jtitle);
            if (description != null)
                description.setText(jdescription);
        } else { // Error
            Picasso.with(context).load(R.drawable.error).error(R.drawable.error).into(image);
        }
    }
}
