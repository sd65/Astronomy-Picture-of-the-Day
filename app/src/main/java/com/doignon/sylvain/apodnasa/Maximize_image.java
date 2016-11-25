package com.doignon.sylvain.apodnasa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class Maximize_image extends AppCompatActivity {

    private GestureDetectorCompat mDetector;
    Maximize_image self;
    ImageView image;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.maximize_image);

        image = (ImageView) findViewById(R.id.image);
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
        self = this;

        Date date = (Date) getIntent().getSerializableExtra("date");
        launchAsyncTask(date);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean helpShown = mPrefs.getBoolean("helpShown", false);
        if (!helpShown) {
            String help = getString(R.string.tutorial);
            new AlertDialog.Builder(this)
                    .setTitle("Welcome to big screen mode !")
                    .setMessage(help)
                    .setIcon(R.drawable.info)
                    .show();
            mPrefs.edit().putBoolean("helpShown", true).apply();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public Date modifyDate(Date d, int i) {
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        c.setTime(d);
        c.add(Calendar.DATE, i);
        Date newDate = c.getTime();
        if (newDate.after(now))
            newDate = now;
        return newDate;
    }

    public void launchAsyncTask(Date date) {
        MyAsyncTask a = new MyAsyncTask();
        a.setContext(self);
        a.setImage(image);
        a.setDate(date);
        a.setForMaximize(true);
        a.execute();
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        private void onSwipeRight() {
            ImageInfo i = (ImageInfo) image.getTag();
            Date d = modifyDate(i.getDate(), 1);
            launchAsyncTask(d);
        }

        private void onSwipeLeft() {
            ImageInfo i = (ImageInfo) image.getTag();
            Date d = modifyDate(i.getDate(), -1);
            launchAsyncTask(d);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                    result = true;
                }
                result = true;

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            self.finish();
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            ImageInfo info = (ImageInfo) image.getTag();
            Intent i = new Intent(Maximize_image.this, Details.class);
            i.putExtra("date", info.getDate());
            startActivity(i);
            return true;
        }


        @Override
        public void onLongPress(MotionEvent e) {
            ImageInfo i = (ImageInfo) image.getTag();
            String msg = "Title: \n " + i.getTitle() + "\n\nDate:\n" + i.getsDate();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(self, msg, duration);
            toast.show();
        }
    }
}
