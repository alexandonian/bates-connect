package com.alexandonian.batesconnect.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandonian.batesconnect.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MingleActivityFragment extends Fragment {

    public MingleActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mingle, container, false);
    }
}
