package com.alexandonian.batesconnect.parser;


import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.alexandonian.batesconnect.MainActivity;
import com.alexandonian.batesconnect.util.CollegeMenu;
import com.alexandonian.batesconnect.util.MenuItem;
import com.alexandonian.batesconnect.util.Util;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Parses the incoming file.
 * <p/>
 * <p>Data is stored in the static fullMenu arraylist of
 * CollegeMenu objects.
 * <p/>
 * <p>Released under GNU GPL v2 - see doc/LICENCES.txt for more info.
 *
 * @author Nicky Ivy parkedraccoon@gmail.com
 */

public class InfoParser {

    public static final String BATES_BASE_URL = "http://www.bates.edu";

    public static final String[] INFO_URL = {
            "/dining/menu/",
            "/events/",
            "/access/building-hours/"
    };

    public final String[] BUILDING_HOURS_URL = {
            "summer-hours/",
            "break-building-hours/",
            "between-semester-building-hours/",
            "semester-building-hours/"
    };

    public static boolean manualRefresh = false;
    public static boolean isBrunch = false;

    public static ArrayList<CollegeMenu> fullMenuObj = new ArrayList<CollegeMenu>() {{
        add(new CollegeMenu());
        add(new CollegeMenu());
        add(new CollegeMenu());

    }};

    public static int getSingleMealList(int info, int month, int day, int year) {

        String[] weekDays = {
                "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Calendar calendar = new GregorianCalendar(year, month - 1, day);
        calendar.setTimeZone(TimeZone.getTimeZone(Util.TIME_ZONE));
        String dayOfWeek = weekDays[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        Log.v(Util.LOG_TAG, "Day of the week: " + dayOfWeek);

//        TO BE USED IF URI BUILDER IS NECESSARY
//        Uri builtUri = Uri.parse(BATES_BASE_URL).buildUpon()
//                .appendQueryParameter("INFO_PARAM",INFO_URL[info])
//                .build();


        Document fullDoc;
        Elements foodNames = null,
                breakfastFoodNames = null,
                lunchFoodNames = null,
                dinnerFoodNames = null,
                brunchFoodNames = null;

        Log.v(Util.LOG_TAG, "Now, we try to fetch fullDoc");
        try {
            fullDoc = Jsoup.connect(BATES_BASE_URL + INFO_URL[info]).get();
        } catch (UnknownHostException e) {
            // Internet connection completely missing is a separate error from okhttp
            Log.v(Util.LOG_TAG, "Internet connection missing");
            e.printStackTrace();
            return Util.GETLIST_INTERNET_FAILURE;
        } catch (IOException e) {
            Log.w(Util.LOG_TAG, "connection error");
            try {
                fullDoc = Jsoup.connect(BATES_BASE_URL + INFO_URL[info]).get();
            } catch (IOException e1) {
                Log.w(Util.LOG_TAG, "connection error");
                try {
                    fullDoc = Jsoup.connect(BATES_BASE_URL + INFO_URL[info]).get();
                } catch (IOException e2) {
                    Log.w(Util.LOG_TAG, "connection error");
                    // Give up after three times
                    return Util.GETLIST_OKHTTP_FAILURE;
                }
            }
        }

//        String bQuery = "#" + dayOfWeek + " ~ div div :contains(Breakfast) li";
//        String lQuery = "#" + dayOfWeek + " ~ div div :contains(Lunch) li";
//        String dQuery = "#" + dayOfWeek + " ~ div div :contains(Dinner) li";

        String CSSQuesry = "#" + dayOfWeek + " ~ div .meal-wrap";

        Log.v(Util.LOG_TAG, "Making Queries");
        foodNames = fullDoc.select(CSSQuesry);
        Log.v(Util.LOG_TAG, "There are " + fullDoc.childNodeSize() + " elements");

        isBrunch = fullDoc.childNodeSize() == 2 || (dayOfWeek.equals(weekDays[0]) ||
                dayOfWeek.equals(weekDays[6]));

        if (isBrunch) {
            brunchFoodNames = foodNames.get(0).select("li");
            dinnerFoodNames = foodNames.get(1).select("li");

            for (int i = 0; i < brunchFoodNames.size(); i++) {
                Log.v(Util.LOG_TAG, brunchFoodNames.get(i).text());
        }

        } else {

            breakfastFoodNames = foodNames.get(0).select("li");
            lunchFoodNames = foodNames.get(1).select("li");
            dinnerFoodNames = foodNames.get(2).select("li");
            Log.v(Util.LOG_TAG, "Queries Complete!!!!!");


            for (int i = 0; i < breakfastFoodNames.size(); i++) {
                Log.v(Util.LOG_TAG, breakfastFoodNames.get(i).text());
            }
            Log.v(Util.LOG_TAG, "---------------------------------------------------------------------");
            for (int i = 0; i < lunchFoodNames.size(); i++) {
                Log.v(Util.LOG_TAG, lunchFoodNames.get(i).text());
            }
        }

        Log.v(Util.LOG_TAG, "---------------------------------------------------------------------");
        for (int i = 0; i < dinnerFoodNames.size(); i++) {
            Log.v(Util.LOG_TAG, dinnerFoodNames.get(i).text());
        }
        ArrayList<MenuItem> breakfastList = new ArrayList<MenuItem>(),
                lunchList = new ArrayList<MenuItem>(),
                dinnerList = new ArrayList<MenuItem>(),
                brunchList = new ArrayList<MenuItem>();


        //Catch if the dining hall is closed for that day
        if (breakfastFoodNames != null && breakfastFoodNames.size() > 0) {
            for (int i = 0; i < breakfastFoodNames.size(); i++) {
                breakfastList.add(new MenuItem(breakfastFoodNames.get(i).text()));
            }
        }

        //Catch if the dining hall is closed for that day
        if (lunchFoodNames != null && lunchFoodNames.size() > 0) {
            for (int i = 0; i < lunchFoodNames.size(); i++) {
                lunchList.add(new MenuItem(lunchFoodNames.get(i).text()));
            }
        }

        //Catch if the dining hall is closed for that day
        if (dinnerFoodNames != null && dinnerFoodNames.size() > 0) {
            for (int i = 0; i < dinnerFoodNames.size(); i++) {
                dinnerList.add(new MenuItem(dinnerFoodNames.get(i).text()));
            }
        }

        //Catch if the dining hall is closed for that day
        if (brunchFoodNames != null && brunchFoodNames.size() > 0) {
            for (int i = 0; i < brunchFoodNames.size(); i++) {
                brunchList.add(new MenuItem(brunchFoodNames.get(i).text()));
            }
        }

        fullMenuObj.get(info).setBreakfast(breakfastList);
        fullMenuObj.get(info).setLunch(lunchList);
        fullMenuObj.get(info).setDinner(dinnerList);
        fullMenuObj.get(info).setBrunch(brunchList);
        if (fullMenuObj.get(info).getBreakfast().isEmpty() &&
                (!(fullMenuObj.get(info).getLunch().isEmpty()) ||
                        !(fullMenuObj.get(info).getDinner().isEmpty()))) {
            ArrayList<MenuItem> breakfastMessage = new ArrayList<MenuItem>();
            breakfastMessage.add(new MenuItem(Util.brunchMessage, "-1"));
            fullMenuObj.get(info).setBreakfast(breakfastMessage);
        }
        return Util.GETLIST_SUCCESS;

    }


    /**
     * /**
     * //     * Puts downloaded data from specified date (instead of today) into the full menu object.
     * //
     */
    public static int getInfoList(int month, int day, int year) {
        for (int i = 0; i < 1; i++) {
            int res = getSingleMealList(i, month, day, year);
            /*
             * For some stupid reason, it throws these stupid unexpected status line errors half the
             * time on mobile data. So we have to intercept those somehow. getsinglemeallist returns
             * okhttp failure if it gets one - and getsinglemeallist also tries multiple times
             * before returning the error. It will only try once for lost internet connection,
             * though.
             */
            if (res == Util.GETLIST_OKHTTP_FAILURE) {
                res = getSingleMealList(i, month, day, year);
                if (res == Util.GETLIST_OKHTTP_FAILURE) {
                    res = getSingleMealList(i, month, day, year);
                    if (res == Util.GETLIST_OKHTTP_FAILURE) {
                        res = getSingleMealList(i, month, day, year);
                        if (res == Util.GETLIST_OKHTTP_FAILURE) {
                            return Util.GETLIST_INTERNET_FAILURE;
                        }
                    }
                }
            } else if (res != Util.GETLIST_SUCCESS) {
                return res;
            }
        }
        return Util.GETLIST_SUCCESS;
    }
}
