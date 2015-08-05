package com.alexandonian.batesconnect.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.hb.views.PinnedSectionListView;

import java.util.ArrayList;

/**
 * Created by Administrator on 8/3/2015.
 */
public class MyPinnedSectionListAdapter extends ArrayAdapter
        implements PinnedSectionListView.PinnedSectionListAdapter {

    // declaring our ArrayList of items
    private ArrayList<String> objects;

    public MyPinnedSectionListAdapter(Context context,int resource, int textViewResourceId, ArrayList<String> objects) {
        super(context, resource, textViewResourceId, objects);
        this.objects = objects;
    }

    @Override
    public boolean isItemViewTypePinned(int i) {
        return false;
    }
}
