package com.alexandonian.batesconnect.adapters;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.alexandonian.batesconnect.R;
import com.alexandonian.batesconnect.util.MenuItem;
import com.alexandonian.batesconnect.util.Util;
import com.hb.views.PinnedSectionListView;

import java.util.ArrayList;

/**
 * Created by Administrator on 8/3/2015.
 */
public class MyPinnedSectionListAdapter extends ArrayAdapter
        implements PinnedSectionListView.PinnedSectionListAdapter {

//    private static final int[] COLORS = new int[] {
//            R.color.green_light, R.color.orange_light,
//            R.color.blue_light, R.color.red_light };

    // declaring our ArrayList of items
    private ArrayList<MenuItem> objects;
    private int mTextViewResourceId;

    public MyPinnedSectionListAdapter(Context context, int resource, int textViewResourceId,
                                      ArrayList<MenuItem> objects) {
        super(context, resource, textViewResourceId, objects);
        this.objects = objects;
        this.mTextViewResourceId = textViewResourceId;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == MenuItem.SECTION;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView mTextView = (TextView) view.findViewById(mTextViewResourceId);
        MenuItem menuItem = getItem(position);
        if (menuItem.getItemType() == MenuItem.SECTION) {
            mTextView.setBackgroundColor(parent.getResources().getColor(R.color
                    .background_material_light));
            mTextView.setTextColor(parent.getResources().getColor(R.color.abc_primary_text_material_light));
            mTextView.setGravity(Gravity.CENTER);
        }
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public MenuItem getItem(int position) {
        return (MenuItem) super.getItem(position);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getItemType();
    }

}
