package com.alexandonian.batesconnect.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alexandonian.batesconnect.R;
import com.alexandonian.batesconnect.activities.MainActivity;
import com.alexandonian.batesconnect.activities.SettingsActivity;
import com.alexandonian.batesconnect.expandingEvents.CustomArrayAdapter;
import com.alexandonian.batesconnect.expandingEvents.ExpandableListItem;
import com.alexandonian.batesconnect.expandingEvents.ExpandingListView;
import com.alexandonian.batesconnect.parser.InfoParser;
import com.alexandonian.batesconnect.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 8/19/2015.
 */
public class EventFragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout
        .OnRefreshListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String NAV_STATE = "nav_number";
    private static final String TAB_STATE = "meal_number";
    private static int mNavState;
    private static int mTabState;
    View rootView = null;


    ArrayAdapter<String> mInfoAdapter;
    List<ExpandableListItem> mAllEvents  = new ArrayList<ExpandableListItem>();
    ListView mListView;
    int[] mDate;


    private SwipeRefreshLayout mSwipeRefreshLayout;

    private final int CELL_DEFAULT_HEIGHT = 200;
    private final int NUM_OF_CELLS = 10;

    private ExpandingListView mEventListView;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static EventFragment newInstance(int eventNumber) {
        EventFragment fragment = new EventFragment();
        mTabState = eventNumber;
        Bundle args = new Bundle();
        args.putInt(TAB_STATE, eventNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public static EventFragment newInstance() {

        return new EventFragment();
    }

    public EventFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        mDate = MainActivity.getRequestedDate();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_infofragment, menu);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateInfo();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
        }

//        if (id == R.id.action_refresh) {
//            updateInfo();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null) {
            mTabState = getArguments().getInt(TAB_STATE);
        }

        rootView = inflater.inflate(R.layout.fragment_events, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id
                .swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private void updateInfo() {
        mSwipeRefreshLayout.setRefreshing(true);
        FetchInfoTask infoTask = new FetchInfoTask();
        infoTask.execute();
    }

    @Override
    public void onRefresh() {
        InfoParser.manualRefresh = true;
        updateInfo();
    }

    public class FetchInfoTask extends AsyncTask<Void, Void, ArrayList<ArrayList<ExpandableListItem>>> {
        private int mNavState, mTabState;
        private int mInitalInfo,
                mAttemptedMonth,
                mAttemptedDay,
                mAttemptedYear;

        private Context mContext;

        private boolean DEBUG = true;
        private boolean mSetPage;

        public FetchInfoTask(int month, int day, int year) {

            this.mAttemptedMonth = month;
            this.mAttemptedDay = day;
            this.mAttemptedYear = year;
//            this.mSetPage = setPage;
//            this.mInitalInfo = initInfo;
//            this.mContext = context;
        }

        public FetchInfoTask() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (getArguments() != null) {
                mTabState = getArguments().getInt(TAB_STATE);
            }
            mAttemptedMonth = mDate[0];
            mAttemptedDay = mDate[1];
            mAttemptedYear = mDate[2];
        }

        @Override
        protected ArrayList<ArrayList<ExpandableListItem>> doInBackground(Void... params) {

//            int res = InfoDataFetcher.fetchData(getActivity(), mAttemptedMonth, mAttemptedDay,
//                    mAttemptedYear);
            int res = 1;

//            return Double.valueOf(res).longValue();
            return InfoParser.getEvents();
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<ExpandableListItem>> result) {
            super.onPostExecute(result);
            Log.v(Util.LOG_TAG, "Entered onPostExecute");
//            if (result == Util.GETLIST_DATABASE_FAILURE) {
//                Toast toast = Toast.makeText(getActivity(), Util.DATABASE_FAILURE, Util
//                        .TOAST_LENGTH);
//                toast.show();
//            }
//
//            if (result == Util.GETLIST_INTERNET_FAILURE) {
//                Toast toast = Toast.makeText(getActivity(), Util.NO_INTERNET, Util.TOAST_LENGTH);
//                toast.show();
//            }
//
//            if (result == Util.GETLIST_OKHTTP_FAILURE) {
//                Toast toast = Toast.makeText(getActivity(), Util.SOMETHING_WRONG, Util
//                        .TOAST_LENGTH);
//                toast.show();
//            }
//
//            if (mSwipeRefreshLayout.isRefreshing()) {
//                mSwipeRefreshLayout.setRefreshing(false);
//                InfoParser.manualRefresh = false;
//            }
//
//
//            if (result == Util.GETLIST_SUCCESS) {
//
//                ExpandableListItem[] values = new ExpandableListItem[]{
//                        new ExpandableListItem("Chameleon", R.drawable.ic_drawer,
//                                CELL_DEFAULT_HEIGHT,
//                                getResources().getString(R.string.dining_menu)),
//                        new ExpandableListItem("Rock", R.drawable.ic_event_black_48dp,
//                                CELL_DEFAULT_HEIGHT,
//
//                                getResources().getString(R.string.events)),
//                        new ExpandableListItem("Flower", R.drawable.ic_drawer,
//                                CELL_DEFAULT_HEIGHT,
//                                getResources().getString(R.string.building_hours)),
//                };
//
//                List<ExpandableListItem> mData = new ArrayList<ExpandableListItem>();
//
//                for (int i = 0; i < NUM_OF_CELLS; i++) {
//                    ExpandableListItem obj = values[i % values.length];
//                    mData.add(new ExpandableListItem(obj.getTitle(), obj.getImgResource(),
//                            obj.getCollapsedHeight(), obj.getText()));
//                }
//
//                CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity()
//                        .getApplicationContext(),
//                        R.layout.event_list_item, mData);
//
//                mEventListView = (ExpandingListView) rootView.findViewById(R.id
//                        .main_list_view);
//
//                mEventListView.setAdapter(adapter);
//                mEventListView.setDivider(null);
//            }
            Log.v(Util.LOG_TAG, "" + mTabState);
                CustomArrayAdapter adapter = new CustomArrayAdapter(rootView.getContext(),
                        R.layout.event_list_item,
                        result.get(mTabState));
                mEventListView = (ExpandingListView) rootView.findViewById(R.id
                            .main_list_view);
                mEventListView.setAdapter(adapter);
                mEventListView.setDivider(null);
            Log.v(Util.LOG_TAG, "onPostExecute completed");

        }
    }

}