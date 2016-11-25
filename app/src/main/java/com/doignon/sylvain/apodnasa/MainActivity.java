package com.doignon.sylvain.apodnasa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SectionsPagerAdapter mSectionsPagerAdapter;
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String website = getString(R.string.website);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                startActivity(browserIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(this, getString(R.string.about_string), duration).show();
            return true;
        }
        if (id == R.id.redotuto) {
            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            mPrefs.edit().putBoolean("helpShown", false).apply();
            String msg = "Click on a picture to launch the tutorial again.";
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(this, msg, duration).show();
            return true;
        }
        if (id == R.id.share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            String psl = getString(R.string.play_store_link);
            sharingIntent.setType("text/plain");
            String shareBody = "Hey ! Look at this great app :\n" + psl;
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Look at this");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static Fragment newInstance(int sectionNumber) {
            Fragment f;
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            if (sectionNumber == 1 || sectionNumber == 2) {
                f = new Presentation();
                Calendar cal = Calendar.getInstance();
                if (sectionNumber == 2)  // It's Yesterday
                    cal.add(Calendar.DATE, -1);
                Date date = cal.getTime();
                args.putSerializable("date", date);
                args.putBoolean("needExtraMargin", true);
            } else {
                f = new PlaceholderFragment();
            }
            f.setArguments(args);
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.tab_datepicker, container, false);
            DatePicker dp = (DatePicker) rootView.findViewById(R.id.datePicker);

            final Calendar myCalendar = Calendar.getInstance();
            int monthOfYear = myCalendar.get(Calendar.MONTH);
            int dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH);
            int year = myCalendar.get(Calendar.YEAR);

            dp.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                    Intent i = new Intent(getContext(), Maximize_image.class);
                    Calendar calendar = Calendar.getInstance();
                    Date now = calendar.getTime();
                    calendar.set(year, month, dayOfMonth);
                    Date newDate = calendar.getTime();
                    if (newDate.after(now))
                        newDate = now;
                    i.putExtra("date", newDate);
                    startActivity(i);
                }
            });

            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.menu_title_1);
                case 1:
                    return getString(R.string.menu_title_2);
                case 2:
                    return getString(R.string.menu_title_3);
            }
            return null;
        }
    }
}
