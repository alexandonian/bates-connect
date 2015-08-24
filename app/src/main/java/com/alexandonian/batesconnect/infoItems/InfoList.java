package com.alexandonian.batesconnect.infoItems;

import java.util.ArrayList;
import java.util.List;

/**
 * College menu object. Keeps things organized,
 * as I would end up using 3-dimensional arraylists otherwise.
 * <p/>
 * <p>Released under GNU GPL v2 - see doc/LICENCES.txt for more info.
 *
 */

public class InfoList {

    private ArrayList<MenuItem> mBreakfast = new ArrayList<MenuItem>();
    private ArrayList<MenuItem> mLunch = new ArrayList<MenuItem>();
    private ArrayList<MenuItem> mDinner = new ArrayList<MenuItem>();
    private ArrayList<MenuItem> mBrunch = new ArrayList<MenuItem>();

    private List<EventItem> mAllEvents = new ArrayList<>();
    private ArrayList<EventItem> mAcademics = new ArrayList<>();
    private ArrayList<EventItem> mArts = new ArrayList<>();
    private ArrayList<EventItem> mAthletics = new ArrayList<>();
    private ArrayList<EventItem> mActivities = new ArrayList<>();



    private boolean isSet = false;

    public InfoList() {
    }

    public InfoList(ArrayList<MenuItem> breakfast, ArrayList<MenuItem> lunch, ArrayList<MenuItem>
            dinner) {
        mBreakfast = breakfast;
        mLunch = lunch;
        mDinner = dinner;
    }

    public InfoList(ArrayList<MenuItem> brunch, ArrayList<MenuItem> dinner) {
        mBrunch = brunch;
        mDinner = dinner;
    }

    public void setBreakfast(ArrayList<MenuItem> meal) {
        mBreakfast = meal;
        isSet = true;
    }

    public void setLunch(ArrayList<MenuItem> meal) {
        mLunch = meal;
        isSet = true;
    }

    public void setDinner(ArrayList<MenuItem> meal) {
        mDinner = meal;
        isSet = true;
    }

    public void setBrunch(ArrayList<MenuItem> meal) {
        mBrunch = meal;
        isSet = true;
    }

    public ArrayList<MenuItem> getBreakfast() {
        if (mBreakfast != null && mBreakfast.size() > 0) {
            return mBreakfast;
        }
        return new ArrayList<MenuItem>();
    }

    public ArrayList<MenuItem> getLunch() {
        if (mLunch != null && mLunch.size() > 0) {
            return mLunch;
        }
        return new ArrayList<MenuItem>();
    }

    public ArrayList<MenuItem> getDinner() {
        if (mDinner != null && mDinner.size() > 0) {
            return mDinner;
        }
        return new ArrayList<MenuItem>();
    }

    public ArrayList<MenuItem> getBrunch() {
        if (mBrunch != null && mBrunch.size() > 0) {
            return mBrunch;
        }
        return new ArrayList<MenuItem>();
    }

    public void setAllEvents(List<EventItem> allEvents) {
        mAllEvents = allEvents;
        isSet = true;
    }

    public void setmAllEvents(List<EventItem> mAllEvents) {
        this.mAllEvents = mAllEvents;
    }

    public void setmAcademics(ArrayList<EventItem> mAcademics) {
        this.mAcademics = mAcademics;
    }

    public void setmArts(ArrayList<EventItem> mArts) {
        this.mArts = mArts;
    }

    public void setmAthletics(ArrayList<EventItem> mAthletics) {
        this.mAthletics = mAthletics;
    }

    public void setmActivities(ArrayList<EventItem> mActivities) {
        this.mActivities = mActivities;
    }

    /**
     * @return Titles of meals as arraylist of strings
     */
    public ArrayList<String> getBreakfastList() {
        ArrayList<String> ret = new ArrayList<String>();
        if (mBreakfast != null && mBreakfast.size() > 0) {
            for (MenuItem m : mBreakfast) {
                ret.add(m.getItemName());
            }
        }
        return ret;
    }

    /**
     * @return Titles of meals as arraylist of strings
     */
    public ArrayList<String> getLunchList() {
        ArrayList<String> ret = new ArrayList<String>();
        if (mLunch != null && mLunch.size() > 0) {
            for (MenuItem m : mLunch) {
                ret.add(m.getItemName());
            }
        }
        return ret;
    }

    /**
     * @return Titles of meals as arraylist of strings
     */
    public ArrayList<String> getDinnerList() {
        ArrayList<String> ret = new ArrayList<String>();
        if (mDinner != null && mDinner.size() > 0) {
            for (MenuItem m : mDinner) {
                ret.add(m.getItemName());
            }
        }
        return ret;
    }

    /**
     * @return Titles of meals as arraylist of strings
     */
    public ArrayList<String> getBrunchList() {
        ArrayList<String> ret = new ArrayList<String>();
        if (mBrunch != null && mBrunch.size() > 0) {
            for (MenuItem m : mBrunch) {
                ret.add(m.getItemName());
            }
        }
        return ret;
    }

    /**
     * Get if data has been written to the
     * object at all
     */
    public boolean getIsSet() {
        return isSet;
    }


    /**
     * Get if the dining hall is open
     * <p/>
     * <p>At this point assume open if nothing has been loaded
     */
    public boolean getIsOpen() {
        if (isSet) {
            if (!getBreakfast().isEmpty() || !getLunch().isEmpty() || !getDinner().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return If the first entry in Dinner contains "college night" (case insensitive) then it's
     * college night
     */
    public boolean getIsCollegeNight() {
        return isSet && getDinner().size() > 0 &&
                getDinner().get(0).getItemName().toLowerCase().contains("college night");
    }

    /**
     * @return If first entry in dinner or lunch is "Healthy Mondays"
     */
    public boolean getIsHealthyMonday() {
        if (isSet) {
            for (MenuItem item : getDinner()) {
                if (item.getItemName().equals("Healthy Mondays")) {
                    return true;
                }
            }
            for (MenuItem item : getLunch()) {
                if (item.getItemName().equals("Healthy Mondays")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return If first entry in dinner or lunch is "Farm Fridays"
     */
    public boolean getIsFarmFriday() {
        if (isSet) {
            if ((getDinner().size() > 0 && getDinner().get(0).getItemName().equals("Farm Fridays")) ||
                    getLunch().size() > 0 && getLunch().get(0).getItemName().equals("Farm Fridays")) {
                return true;
            }
        }
        return false;
    }
}