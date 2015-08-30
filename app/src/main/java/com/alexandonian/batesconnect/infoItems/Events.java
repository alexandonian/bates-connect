package com.alexandonian.batesconnect.infoItems;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 8/10/2015.
 */
public class Events {

    private List<EventItem> mAllEvents = new ArrayList<>();
    private ArrayList<EventItem> mAcademics = new ArrayList<>();
    private ArrayList<EventItem> mArts = new ArrayList<>();
    private ArrayList<EventItem> mAthletics = new ArrayList<>();
    private ArrayList<EventItem> mActivities = new ArrayList<>();

    private boolean isSet = false;

    public Events() {
    }

    public Events(ArrayList<EventItem> all, ArrayList<EventItem> academics,
                  ArrayList<EventItem> arts, ArrayList<EventItem> athletics,
                  ArrayList<EventItem> activities) {
        mAllEvents = all;
        mAcademics = academics;
        mArts = arts;
        mAthletics = athletics;
        mActivities = activities;

    }

    public List<EventItem> getAllEvents() {
        return mAllEvents;
    }

    public void setAllEvents(List<EventItem> mAllEvents) {
        this.mAllEvents = mAllEvents;
    }

    public ArrayList<EventItem> getAcademics() {
        return mAcademics;
    }

    public void setAcademics(ArrayList<EventItem> mAcademics) {
        this.mAcademics = mAcademics;
    }

    public ArrayList<EventItem> getArts() {
        return mArts;
    }

    public void setArts(ArrayList<EventItem> mArts) {
        this.mArts = mArts;
    }

    public ArrayList<EventItem> getAthletics() {
        return mAthletics;
    }

    public void setAthletics(ArrayList<EventItem> mAthletics) {
        this.mAthletics = mAthletics;
    }

    public ArrayList<EventItem> getActivities() {
        return mActivities;
    }

    public void setActivities(ArrayList<EventItem> mActivities) {
        this.mActivities = mActivities;
    }
}
