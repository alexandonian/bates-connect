package com.alexandonian.batesconnect.fragments;

import android.app.Activity;
import android.app.Fragment;
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
import com.alexandonian.batesconnect.activities.MainActivity;
import com.alexandonian.batesconnect.activities.SettingsActivity;
import com.alexandonian.batesconnect.adapters.MyPinnedSectionListAdapter;
import com.alexandonian.batesconnect.parser.InfoDataFetcher;
import com.alexandonian.batesconnect.parser.InfoParser;
import com.alexandonian.batesconnect.util.Util;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout
        .OnRefreshListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String NAV_NUMBER = "nav_number";
    private static final String MEAL_NUMBER = "meal_number";

    ArrayAdapter<String> mInfoAdapter;
    ListView mListView;
    int[] mDate;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static InfoFragment newInstance(int navNumber, int mealNumber) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putInt(NAV_NUMBER, navNumber);
        args.putInt(MEAL_NUMBER, mealNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public static InfoFragment newInstance() {

        return new InfoFragment();
    }

    public InfoFragment() {
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
        inflater.inflate(R.menu.infofragment, menu);
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

//        String[] items = {"Oatmeal", "Toast", "Pancakes"};

//        List<String> info = new ArrayList<>(Arrays.asList(items));
//
//        mInfoAdapter =
//                new ArrayAdapter<String>(
//                        getActivity(),
//                        R.layout.list_item_info,
//                        R.id.list_item_info_textview,
//                        info);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mListView = (ListView) rootView.findViewById(R.id.listview_info);
//        mListView.setAdapter(mInfoAdapter);


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getArguments() != null) {
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(NAV_NUMBER));
        }
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

    public class FetchInfoTask extends AsyncTask<Void, Void, Long> {
        private int mNavNumber, mMealNumber;
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
                mNavNumber = getArguments().getInt(NAV_NUMBER);
                mMealNumber = getArguments().getInt(MEAL_NUMBER);
            }
            mAttemptedMonth = mDate[0];
            mAttemptedDay = mDate[1];
            mAttemptedYear = mDate[2];
        }

        @Override
        protected Long doInBackground(Void... params) {

            int res = InfoDataFetcher.fetchData(getActivity(), mAttemptedMonth, mAttemptedDay,
                    mAttemptedYear);

            return Double.valueOf(res).longValue();
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

            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
                InfoParser.manualRefresh = false;
            }

            ArrayList<com.alexandonian.batesconnect.util.MenuItem> meal = null;


            if (result == Util.GETLIST_SUCCESS) {
                if (MainActivity.isBrunch()) {
                    switch (mMealNumber) {
                        case 0:
                            meal = InfoParser.fullMenuObj.get(mNavNumber).getBrunch();
                            break;
                        case 1:
                            meal = InfoParser.fullMenuObj.get(mNavNumber).getDinner();
                            break;
                        default:
                            meal = InfoParser.fullMenuObj.get(mNavNumber).getBrunch();
                    }
                } else {
                    switch (mMealNumber) {
                        case 0:
                            meal = InfoParser.fullMenuObj.get(mNavNumber).getBreakfast();
                            break;
                        case 1:
                            meal = InfoParser.fullMenuObj.get(mNavNumber).getLunch();
                            break;
                        case 2:
                            meal = InfoParser.fullMenuObj.get(mNavNumber).getDinner();
                            break;
                        default:
                            meal = InfoParser.fullMenuObj.get(mNavNumber).getBreakfast();
                    }
                }

                if (mListView != null) {
    //                mInfoAdapter.clear();
    //                mInfoAdapter.addAll(meal);
                    mInfoAdapter =
                            new MyPinnedSectionListAdapter(
                                    getActivity(),
                                    R.layout.list_item_info,
                                    R.id.list_item_info_textview,
                                    meal);
                    mListView.setAdapter(mInfoAdapter);
    //                listView.setAdapter(new ArrayAdapter<String>(
    //                        getActivity(),
    //                        R.layout.list_item_info,
    //                        R.id.list_item_info_textview,
    //                        meal));
                }
            }
        }
    }

}


