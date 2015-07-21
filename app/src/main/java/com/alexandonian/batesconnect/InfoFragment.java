package com.alexandonian.batesconnect;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alexandonian.batesconnect.parser.MenuParser;
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
    private static final String ARG_SECTION_NUMBER = "section_number";

    ArrayAdapter<String> mInfoAdapter;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static InfoFragment newInstance(int sectionNumber) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            Log.v("InfoFragment", "Refresh Button Pressed");
            FetchInfoTask infoTask = new FetchInfoTask();
            infoTask.execute();
            return true;
        }
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

        ListView listView = (ListView) rootView.findViewById(R.id.listview_info);
        listView.setAdapter(mInfoAdapter);


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public class FetchInfoTask extends AsyncTask<Void, Void,
            ArrayList<com.alexandonian.batesconnect.util.MenuItem>> {

        private boolean DEBUG = true;

        @Override
        protected ArrayList<com.alexandonian.batesconnect.util.MenuItem> doInBackground(Void... params) {
            Log.v(Util.LOG_TAG, "doInBackground has started");

            return MenuParser.getSingleMealList(0, 7, 21, 2015);
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            super.onPostExecute(arrayList);
            Log.v(Util.LOG_TAG, "onPostExecute");

        }
    }

}


