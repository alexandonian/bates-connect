package com.alexandonian.batesconnect.expandingEvents;

/**
 * Created by Administrator on 8/11/2015.
 */
import android.app.Activity;
import android.os.Bundle;

import com.alexandonian.batesconnect.R;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity creates a listview whose items can be clicked to expand and show
 * additional content.
 *
 * In this specific demo, each item in a listview displays an image and a corresponding
 * title. These two items are centered in the default (collapsed) state of the listview's
 * item. When the item is clicked, it expands to display text of some varying length.
 * The item persists in this expanded state (even if the user scrolls away and then scrolls
 * back to the same location) until it is clicked again, at which point the cell collapses
 * back to its default state.
 */
public class ExpandingCells extends Activity {

    private final int CELL_DEFAULT_HEIGHT = 200;
    private final int NUM_OF_CELLS = 30;

    private ExpandingListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExpandableListItem[] values = new ExpandableListItem[] {
                new ExpandableListItem("Chameleon", R.drawable.bates_connect_icon, CELL_DEFAULT_HEIGHT,
                        getResources().getString(R.string.dining_menu)),
                new ExpandableListItem("Rock", R.drawable.bates_connect_icon, CELL_DEFAULT_HEIGHT,
                        getResources().getString(R.string.events)),
                new ExpandableListItem("Flower", R.drawable.bates_connect_icon, CELL_DEFAULT_HEIGHT,
                        getResources().getString(R.string.building_hours)),
        };

        List<ExpandableListItem> mData = new ArrayList<ExpandableListItem>();

        for (int i = 0; i < NUM_OF_CELLS; i++) {
            ExpandableListItem obj = values[i % values.length];
            mData.add(new ExpandableListItem(obj.getTitle(), obj.getImgResource(),
                    obj.getCollapsedHeight(), obj.getText()));
        }

        CustomArrayAdapter adapter = new CustomArrayAdapter(this, R.layout.event_list_item, mData);

        mListView = (ExpandingListView)findViewById(R.id.main_list_view);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);
    }
}