package com.alexandonian.batesconnect.activities;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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
import android.widget.TextView;

import com.alexandonian.batesconnect.R;
import com.alexandonian.batesconnect.demo.Login;
import com.alexandonian.batesconnect.demo.UserList;
import com.alexandonian.batesconnect.demo.utils.Utils;
import com.alexandonian.batesconnect.fragments.BuildingHoursFragment;
import com.alexandonian.batesconnect.fragments.EventFragment;
import com.alexandonian.batesconnect.fragments.MenuFragment;
import com.alexandonian.batesconnect.fragments.NavigationDrawerFragment;
import com.alexandonian.batesconnect.tabs.SlidingTabLayout;
import com.alexandonian.batesconnect.util.Util;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.software.shell.fab.ActionButton;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static Context mContext;

    // UI Elements
    private static Toolbar toolbar;
    private static ViewPager mPager;
    private static FragmentStatePagerAdapter mPagerAdapter;
    private ActionButton mActionButton;
    private static TextView mDateTextView;
    private static SlidingTabLayout mTabs;
    private static String[] tabs;
    private static String[] tabs_events;
    private static String[] tabs_buildingHours;


    // UI State
    private static String TITLE = "title";
    private static String DATE_STATE = "date_state";
    private static String NAV_STATE = "nav_state";
    private static String TAB_STATE = "tab_state";
    private static int[] mDate;
    private static int mNavState;
    private static int mTabState;

    private static int DINING_MENU = 0;
    private static int EVENTS = 1;
    private static int BUILDING_HOURS = 2;

    // Fragment Arrays
    private static ArrayList<Fragment> mMenuFragments = new ArrayList<>();
    private static ArrayList<Fragment> mEventFragments = new ArrayList<>();
    private static ArrayList<Fragment> mBuildingFragments = new ArrayList<>();


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

        mDate = Util.getToday();
        mDateTextView = (TextView) findViewById(R.id.date_textview);
        mDateTextView.setText(Util.getDayOfWeek(mDate[0], mDate[1], mDate[2]) + ", " + Util
                .getMonthName(mDate[0]) + " " + mDate[1] + ", " + mDate[2]);
        setupDrawer();
        setupTabs();
        setupFAB();
        updatePagerAdapter();
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

    public void setupTabs() {
        mPager = (ViewPager) findViewById(R.id.pager);

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(0);
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true);
        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.bates_tab_selector));
        mTabs.setViewPager(mPager);
    }

    private void setupFAB() {

        mActionButton = (ActionButton) findViewById(R.id.action_button);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

//    private void setupFragments(int INFO) {
//        switch (INFO) {
//            case 0:
//                for (int i = 0; i < 3; i++)
//                    mMenuFragments.add(MenuFragment.newInstance(i));
//                break;
//            case 1:
//                for (int i = 0; i < 5; i++)
//                    mEventFragments.add(EventFragment.newInstance(i));
//                break;
//            case 2:
//                for (int i = 0; i < 1; i++)
//                    mBuildingFragments.add(MenuFragment.newInstance(i));
//                break;
//            default:
//                for (int i = 0; i < 3; i++)
//                    mMenuFragments.add(MenuFragment.newInstance(i));
//                break;
//        }
//
//    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        mNavState = 0;
        onNavigationDrawerItemSelected(mNavState);
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
    protected void onSaveInstanceState(Bundle outState) {
        // Save the user's current state
        outState.putCharSequence(TITLE, mTitle);
        outState.putInt(NAV_STATE, mNavState);
        outState.putInt(TAB_STATE, mTabState);
        outState.putIntArray(DATE_STATE, mDate);


        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        mTitle = savedInstanceState.getCharSequence(TITLE);
        mNavState = savedInstanceState.getInt(NAV_STATE);
        mTabState = savedInstanceState.getInt(TAB_STATE);
        mDate = savedInstanceState.getIntArray(DATE_STATE);
        mDateTextView.setText(Util.getDayOfWeek(mDate[0], mDate[1], mDate[2]) + ", " + Util
                .getMonthName(mDate[0]) + " " + mDate[1] + ", " + mDate[2]);
        updatePagerAdapter();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
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


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the menu_main content by replacing fragments

        switch (position) {
            case 0:
                mTitle = getString(R.string.dining_menu);
                if (mActionButton != null) {
                    mActionButton.show();
                }
                if (mDateTextView != null) {
                    mDateTextView.setHeight(65);
                }
                updatePagerAdapter();

                break;
            case 1:
                mTitle = getString(R.string.events);
                if (mActionButton != null) {
                    mActionButton.hide();
                }
                updatePagerAdapter();
                mDate = Util.getToday();
                mDateTextView = (TextView) findViewById(R.id.date_textview);
                mDateTextView.setText(Util.getDayOfWeek(mDate[0], mDate[1], mDate[2]) + ", " + Util
                        .getMonthName(mDate[0]) + " " + mDate[1] + ", " + mDate[2]);
                mDateTextView.setHeight(0);
                break;
            case 2:
                mTitle = getString(R.string.building_hours);
                if (mActionButton != null) {
                    mActionButton.hide();
                }
                updatePagerAdapter();
                mDate = Util.getToday();
                mDateTextView = (TextView) findViewById(R.id.date_textview);
                mDateTextView.setText(Util.getDayOfWeek(mDate[0], mDate[1], mDate[2]) + ", " + Util
                        .getMonthName(mDate[0]) + " " + mDate[1] + ", " + mDate[2]);
                mDateTextView.setHeight(0);
                break;
            case 3:
                mPagerAdapter.notifyDataSetChanged();

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

                if (sp.getBoolean(Login.PREF_USER_VERIFIED, false)) {
                    String mUserEmail = sp.getString(Login.PREF_USER_EMAIL, "");
                    if (mUserEmail.length() == 0) {
                        Utils.showDialog(this, R.string.err_fields_empty);
                        return;
                    }
                    final ProgressDialog dia = ProgressDialog.show(this, null, getString(R.string
                            .alert_login));
                    ParseUser.logInInBackground(mUserEmail, mUserEmail, new LogInCallback() {

                        @Override
                        public void done(ParseUser pu, ParseException e) {
                            dia.dismiss();
                            if (pu != null) {
                                UserList.user = pu;
                                startActivity(new Intent(MainActivity.this, UserList.class));
                                finish();
                            } else {
                                Utils.showDialog(
                                        MainActivity.this,
                                        getString(R.string.err_login) + " "
                                                + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    startActivity(new Intent(this, Login.class));
                    break;
                }
                default:


        }
    }

    public static void updatePagerAdapter() {
        if (mPagerAdapter != null) {
            mPagerAdapter.notifyDataSetChanged();
            mPager.setAdapter(mPagerAdapter);
            mPager.setCurrentItem(0);
            mTabs.setDistributeEvenly(true);
            mTabs.setViewPager(mPager);
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.dining_menu);
                if (mActionButton != null) {
                    mActionButton.show();
                }
                break;
            case 1:
                mTitle = getString(R.string.events);
                break;
            case 2:
                mTitle = getString(R.string.building_hours);
                break;
        }
    }

    public static int getNavState() {
        return mNavState;
    }

    public static int getTabState() {
        return mTabState;
    }

    public static void setTabState(int tabState) {
        mTabState = tabState;
    }

    public static void setNavState(int navState) {
        mNavState = navState;
    }

    public static int[] getRequestedDate() {
        return mDate;
    }

    public static void setRequestedDate(int month, int day, int year) {

        mDate[0] = month;
        mDate[1] = day;
        mDate[2] = year;

        mDateTextView.setText(Util.getDayOfWeek(month, day, year) + ", " + Util.getMonthName
                (month) +
                " " + day + ", " + mDate[2]);
        updatePagerAdapter();
    }

    public static boolean isBrunch() {

        int[] date = MainActivity.getRequestedDate();
        if (date != null) {
            String dayOfWeek = Util.getDayOfWeek(date[0], date[1], date[2]);
            return (dayOfWeek.equals("Saturday") || dayOfWeek.equals("Sunday"));
        } else {
            date = Util.getToday();
            String dayOfWeek = Util.getDayOfWeek(date[0], date[1], date[2]);
            return (dayOfWeek.equals("Saturday") || dayOfWeek.equals("Sunday"));
        }
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            if (mNavState == 1) {
                tabs = getResources().getStringArray(R.array.tabs_events);
            }
            tabs = MainActivity.isBrunch() ?
                    getResources().getStringArray(R.array.tabs_brunch) :
                    getResources().getStringArray(R.array.tabs_meal);
        }

        @Override
        public Fragment getItem(int position) {

            switch (MainActivity.getNavState()) {
                case 0:
//                    return mMenuFragments.get(position);
                    return MenuFragment.newInstance(position);
                case 1:
                    return EventFragment.newInstance(position);
                default:
                    return BuildingHoursFragment.newInstance(position);
            }
        }

        public CharSequence getPageTitle(int position) {

            switch (mNavState) {
                case 0:
                    return tabs[position];
                case 1:
                    tabs_events = getResources().getStringArray(R.array.tabs_events);
                    return tabs_events[position];
                case 2:
                    tabs_buildingHours = getResources().getStringArray(R.array.tabs_buildingHours);
                    return tabs_buildingHours[position];
                default:
                    return tabs[position];
            }
        }

        @Override
        public int getCount() {
            switch (mNavState) {
                case 0:
                    return MainActivity.isBrunch() ? 2 : 3;
                case 1:
                    return 5;
                case 2:
                    return 4;
                case 3:
                    return 1;
                default:
                    return 1;
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();

            switch (mNavState) {
                case 0:
                    tabs = MainActivity.isBrunch() ?
                            getResources().getStringArray(R.array.tabs_brunch) :
                            getResources().getStringArray(R.array.tabs_meal);
                case 1:
                    tabs = getResources().getStringArray(R.array.tabs_events);
                default:
                    tabs = getResources().getStringArray(R.array.tabs_meal);
            }
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker

            int year = mDate[2];
            int month = mDate[0] - 1;
            int day = mDate[1];

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            setRequestedDate(month + 1, day, year);
        }


    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}