package com.alexandonian.batesconnect.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView mTextView = (TextView) view.findViewById(mTextViewResourceId);
        MenuItem menuItem = getItem(position);

        // PINNED SECTION
        if (menuItem.getItemType() == MenuItem.SECTION) {
            mTextView.setBackground(parent.getResources().getDrawable(R.drawable.rounded_corners));
//            mTextView.setBackgroundColor(parent.getResources().getColor(R.color
//                    .background_material_light));
            mTextView.setTextColor(parent.getResources().getColor(R.color.fab_material_white));
//            mTextView.setTextColor(parent.getResources().getColor(R.color
// .abc_primary_text_material_light));
            mTextView.setGravity(Gravity.CENTER);
        }


        // GLUTEN Free/Friendly ITEM
        Drawable glutenDrawable = parent.getResources().getDrawable(R.drawable.gluten_free);
        Drawable veganDrawable = parent.getResources().getDrawable(R.drawable.vegan_icon);
        glutenDrawable.setBounds(0, 0, 100, 100);
        veganDrawable.setBounds(0, 0, 100, 100);
        if (Util.isGluten(menuItem.getItemName())) {
            Log.v(Util.LOG_TAG, menuItem.getItemName());
            mTextView.setCompoundDrawables(null, null, glutenDrawable, null);
        } else if (Util.isVegan(menuItem.getItemName())) {
            Log.v(Util.LOG_TAG, menuItem.getItemName());
            mTextView.setCompoundDrawables(null, null, veganDrawable, null);
        } else {
            mTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
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
