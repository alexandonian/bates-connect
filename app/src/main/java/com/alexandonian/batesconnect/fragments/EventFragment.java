package com.alexandonian.batesconnect.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alexandonian.batesconnect.R;
import com.alexandonian.batesconnect.activities.SettingsActivity;
import com.alexandonian.batesconnect.adapters.ExpandableEventAdapter;
import com.alexandonian.batesconnect.expandingEvents.ExpandingListView;
import com.alexandonian.batesconnect.infoItems.EventItem;
import com.alexandonian.batesconnect.parser.InfoDataFetcher;
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
    List<EventItem> mAllEvents  = new ArrayList<EventItem>();
    ListView mListView;



    private SwipeRefreshLayout mSwipeRefreshLayoutEvents;

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
        mSwipeRefreshLayoutEvents = (SwipeRefreshLayout) rootView.findViewById(R.id
                .swipeRefreshLayoutEvents);
        mSwipeRefreshLayoutEvents.setOnRefreshListener(this);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private void updateInfo() {
        mSwipeRefreshLayoutEvents.setRefreshing(true);
        FetchInfoTask infoTask = new FetchInfoTask(getActivity());
        infoTask.execute();
    }

    @Override
    public void onRefresh() {
        InfoParser.manualRefresh = true;
        updateInfo();
    }

    public class FetchInfoTask extends AsyncTask<Void, Void, Long> {
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
        }

        public FetchInfoTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (getArguments() != null) {
                mTabState = getArguments().getInt(TAB_STATE);
            }
        }

        @Override
        protected Long doInBackground(Void... params) {

            return Double.valueOf(InfoDataFetcher.fetchEvents(mContext)).longValue();
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            if (result == Util.GETLIST_DATABASE_FAILURE) {
                Toast toast = Toast.makeText(getActivity(), Util.DATABASE_FAILURE, Util
                        .TOAST_LENGTH);
                toast.show();
            }
            if (result == Util.GETLIST_INTERNET_FAILURE) {
                Toast toast = Toast.makeText(getActivity(), Util.NO_INTERNET, Util.TOAST_LENGTH);
                toast.show();
            }
            if (result == Util.GETLIST_OKHTTP_FAILURE) {
                Toast toast = Toast.makeText(getActivity(), Util.SOMETHING_WRONG, Util
                        .TOAST_LENGTH);
                toast.show();
            }
            if (mSwipeRefreshLayoutEvents.isRefreshing()) {
                mSwipeRefreshLayoutEvents.setRefreshing(false);
                InfoParser.manualRefresh = false;
            }

                ExpandableEventAdapter adapter = new ExpandableEventAdapter(rootView.getContext(),
                        R.layout.event_list_item,
                        InfoParser.EVENTS.get(mTabState));
                mEventListView = (ExpandingListView) rootView.findViewById(R.id
                            .main_list_view);
                mEventListView.setAdapter(adapter);
                mEventListView.setDivider(null);

        }
    }

}