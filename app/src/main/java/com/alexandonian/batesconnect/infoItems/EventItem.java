package com.alexandonian.batesconnect.infoItems;

import com.alexandonian.batesconnect.expandingEvents.OnSizeChangedListener;

/**
 * This custom object is used to populate the list adapter. It contains a reference
 * to an image, title, and the extra text to be displayed. Furthermore, it keeps track
 * of the current state (collapsed/expanded) of the corresponding item in the list,
 * as well as store the height of the cell in its collapsed state.
 */
public class EventItem implements OnSizeChangedListener {

    private String mTitle;
    private String mDate;
    private String mCategory;



    private String mDescription;
    private boolean mIsExpanded;
    private int mImgResource;
    private int mCollapsedHeight;
    private int mExpandedHeight;

    public EventItem() {

    }

    public EventItem(String title, int imgResource, int collapsedHeight, String text) {
        mTitle = title;
        mImgResource = imgResource;
        mCollapsedHeight = collapsedHeight;
        mIsExpanded = false;
        mDescription = text;
        mExpandedHeight = -1;
    }

    public EventItem(String title, String date, int imgResource, int collapsedHeight,
                     String description) {
        mTitle = title;
        mDate = date;
        mImgResource = imgResource;
        mCollapsedHeight = collapsedHeight;
        mIsExpanded = false;
        mDescription = description;
        mExpandedHeight = -1;
    }

    public boolean isExpanded() {
        return mIsExpanded;
    }

    public void setExpanded(boolean isExpanded) {
        mIsExpanded = isExpanded;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getImgResource() {
        return mImgResource;
    }

    public int getCollapsedHeight() {
        return mCollapsedHeight;
    }

    public void setCollapsedHeight(int collapsedHeight) {
        mCollapsedHeight = collapsedHeight;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getExpandedHeight() {
        return mExpandedHeight;
    }

    public void setExpandedHeight(int expandedHeight) {
        mExpandedHeight = expandedHeight;
    }

    @Override
    public void onSizeChanged(int newHeight) {
        setExpandedHeight(newHeight);
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String Date) {
        this.mDate = Date;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }
}
