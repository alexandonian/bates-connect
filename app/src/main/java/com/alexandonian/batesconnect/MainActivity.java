package com.alexandonian.batesconnect;



import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.alexandonian.batesconnect.InfoFragments.InfoFragment;
import com.alexandonian.batesconnect.tabs.SlidingTabLayout;
import com.alexandonian.batesconnect.util.Util;
import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private Toolbar toolbar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;

    private String[] tabs;
    private String NAV_NUMBER = "nav_number";
    private String MEAL_NUMBER = "meal_number";
    private static int mNavNumber;
    private static int mMealNumber;
    public ArrayList<Fragment> mMenuFragments = new ArrayList<>();
    public ActionButton mActionButton;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_appbar);
        setupDrawer();
        setupTabs();
        setupFragments();
        setupFAB();
    }

    private void setupDrawer() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                toolbar);
    }

    private void setupTabs() {
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mPager.setCurrentItem(0);
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true);
        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.color_accent));
        mTabs.setViewPager(mPager);
    }

    private void setupFAB() {
//        ImageView imageView = new ImageView(this);
//        imageView.setImageResource(R.drawable.ic_event_black_48dp);
//
//              FloatingActionButton mActionButton = new FloatingActionButton.Builder(this)
//                .setContentView(imageView)
//                .build();
//        Context context = getApplicationContext();
//        ActionButton mActionButton = new ActionButton(context);
//        mActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_event_black_48dp));

        ActionButton mActionButton = (ActionButton) findViewById(R.id.action_button);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "FAB button pressed", Util.TOAST_LENGTH);
                toast.show();
                showDatePickerDialog();
            }
        });

    }

    private void setupFragments() {
        mMenuFragments.add(InfoFragment.newInstance(0, 0));
        mMenuFragments.add(InfoFragment.newInstance(0, 1));
        mMenuFragments.add(InfoFragment.newInstance(0, 2));
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the menu_main content by replacing fragments

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = InfoFragment.newInstance();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        switch (position) {
            case 0:
                mTitle = getString(R.string.dining_menu);
                break;
            case 1:
                mTitle = getString(R.string.events);
                break;
            case 2:
                mTitle = getString(R.string.building_hours);
                break;
        }
    }


    public void onSectionAttached(int number) {
        Log.v(Util.LOG_TAG, "onSectionAttached number: " + number);
        mNavNumber = number;
        switch (number) {
            case 0:
                mTitle = getString(R.string.dining_menu);
                break;
            case 1:
                mTitle = getString(R.string.events);
                break;
            case 2:
                mTitle = getString(R.string.building_hours);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public static int getNavNumber() {
        return mNavNumber;
    }

    public static int getMealNumber() {
        return mMealNumber;
    }

    public static void setNavNumber(int navNumber) {
        mNavNumber = navNumber;
    }


    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = Util.isBrunch ?
                    getResources().getStringArray(R.array.tabs_brunch) :
                    getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            mMealNumber = position;
            Log.v(Util.LOG_TAG, "getItem position: " + position);
            Log.v(Util.LOG_TAG, "mMealNumber: " + mMealNumber);

            return mMenuFragments.get(position);

        }


        public CharSequence getPageTitle(int position) {
            return tabs[position];

        }

        @Override
        public int getCount() {
            return Util.isBrunch ? 2 : 3;
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
        }


    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}