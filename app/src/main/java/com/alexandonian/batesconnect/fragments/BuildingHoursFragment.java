package com.alexandonian.batesconnect.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.alexandonian.batesconnect.util.MyWebViewClient;
import com.alexandonian.batesconnect.R;
import com.alexandonian.batesconnect.activities.MainActivity;
import com.alexandonian.batesconnect.activities.SettingsActivity;
import com.alexandonian.batesconnect.parser.InfoParser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BuildingHoursFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BuildingHoursFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuildingHoursFragment extends android.support.v4.app.Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String NAV_STATE = "nav_number";
    private static final String TAB_STATE = "meal_number";
    private static int mNavState;
    private static int mTabState;
    private WebView mWebView;
    View rootView = null;


    int[] mDate;


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static BuildingHoursFragment newInstance(int bhNumber) {
        BuildingHoursFragment fragment = new BuildingHoursFragment();
        mTabState = bhNumber;
        Bundle args = new Bundle();
        args.putInt(TAB_STATE, bhNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public static BuildingHoursFragment newInstance() {

        return new BuildingHoursFragment();
    }

    public BuildingHoursFragment() {
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

        rootView = inflater.inflate(R.layout.fragment_buildinghours, container, false);
        mWebView = (WebView) rootView.findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient(mWebView));
        mWebView.loadUrl(InfoParser.BATES_BASE_URL + InfoParser.INFO_URL[2] + InfoParser
                .BUILDING_HOURS_URL[mTabState]);
//        mWebView.setVisibility(View.VISIBLE);

//        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id
//                .swipeRefreshLayout);
//        mSwipeRefreshLayout.setOnRefreshListener(this);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


}


