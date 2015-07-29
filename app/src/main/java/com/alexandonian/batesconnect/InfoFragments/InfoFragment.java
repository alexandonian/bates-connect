package com.alexandonian.batesconnect.InfoFragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alexandonian.batesconnect.MainActivity;
import com.alexandonian.batesconnect.R;
import com.alexandonian.batesconnect.SettingsActivity;
import com.alexandonian.batesconnect.parser.InfoDataFetcher;
import com.alexandonian.batesconnect.parser.InfoParser;
import com.alexandonian.batesconnect.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends android.support.v4.app.Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String NAV_NUMBER = "nav_number";
    private static final String MEAL_NUMBER = "meal_number";

    ArrayAdapter<String> mInfoAdapter;
    ListView mListView;
    int[] today;

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


        String[] items = {"Oatmeal", "Toast", "Pancakes"};

        List<String> info = new ArrayList<>(Arrays.asList(items));

        mInfoAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.list_item_info,
                        R.id.list_item_info_textview,
                        info);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mListView = (ListView) rootView.findViewById(R.id.listview_info);
        mListView.setAdapter(mInfoAdapter);


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
        today = Util.getToday();
        FetchInfoTask infoTask = new FetchInfoTask(today[0], today[1], today[2]);
        infoTask.execute();
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (getArguments() != null) {
                mNavNumber = getArguments().getInt(NAV_NUMBER);
                mMealNumber = getArguments().getInt(MEAL_NUMBER);
            }
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
                Toast toast = Toast.makeText(getActivity(), Util.DATABASE_FAILURE, Util.TOAST_LENGTH);
                toast.show();
            }

            if (result == Util.GETLIST_INTERNET_FAILURE) {
                Toast toast = Toast.makeText(getActivity(), Util.NO_INTERNET, Util.TOAST_LENGTH);
                toast.show();
            }

            if (result == Util.GETLIST_OKHTTP_FAILURE) {
                Toast toast = Toast.makeText(getActivity(), Util.SOMETHING_WRONG, Util.TOAST_LENGTH);
                toast.show();
            }

            ArrayList<String> meal;

            if (Util.isBrunch) {
                switch (mMealNumber) {
                    case 0:
                        meal = InfoParser.fullMenuObj.get(mNavNumber).getBrunchList();
                        break;
                    case 1:
                        meal = InfoParser.fullMenuObj.get(mNavNumber).getDinnerList();
                        break;
                    default:
                        meal = InfoParser.fullMenuObj.get(mNavNumber).getBrunchList();
                }
            } else {
                switch (mMealNumber) {
                    case 0:
                        meal = InfoParser.fullMenuObj.get(mNavNumber).getBreakfastList();
                        break;
                    case 1:
                        meal = InfoParser.fullMenuObj.get(mNavNumber).getLunchList();
                        break;
                    case 2:
                        meal = InfoParser.fullMenuObj.get(mNavNumber).getDinnerList();
                        break;
                    default:
                        meal = InfoParser.fullMenuObj.get(mNavNumber).getBreakfastList();
                }
            }

            if (mListView != null) {
                mInfoAdapter.clear();
                mInfoAdapter.addAll(meal);
//                listView.setAdapter(new ArrayAdapter<String>(
//                        getActivity(),
//                        R.layout.list_item_info,
//                        R.id.list_item_info_textview,
//                        meal));
            }
        }
    }

}

