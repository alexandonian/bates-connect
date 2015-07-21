package com.alexandonian.batesconnect.parser;


import android.util.Log;

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

/**
 * Parses the incoming file.
 *
 * <p>Data is stored in the static fullMenu arraylist of
 * CollegeMenu objects.
 *
 * <p>Released under GNU GPL v2 - see doc/LICENCES.txt for more info.
 *
 * @author Nicky Ivy parkedraccoon@gmail.com
 */

public class MenuParser {

    public static final String BASE_URL = "http://www.bates.edu";

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

    public static ArrayList<CollegeMenu> fullMenuObj = new ArrayList<CollegeMenu>() {{
        add(new CollegeMenu());
        add(new CollegeMenu());
        add(new CollegeMenu());

    }};

    public static ArrayList<MenuItem> getSingleMealList(int k, int month, int day, int year) {

        String[] weekDays = {
                "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Calendar calendar = new GregorianCalendar(year, month - 1, day);
        String dayOfWeek = weekDays[calendar.get(Calendar.DAY_OF_WEEK) - 1];


        Document fullDoc;
        Elements breakfastFoodNames = null,
                lunchFoodNames = null,
                dinnerFoodNames = null;

        Log.v(Util.LOG_TAG,"Now, we try to fetch fullDoc");
        try {
            fullDoc = Jsoup.connect(BASE_URL + INFO_URL[k]).get();
        } catch (UnknownHostException e) {
            // Internet connection completely missing is a separate error from okhttp
            Log.v(Util.LOG_TAG, "Internet connection missing");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            Log.w(Util.LOG_TAG, "connection error");
            try {
                fullDoc = Jsoup.connect(BASE_URL + INFO_URL[k]).get();
            } catch (IOException e1) {
                Log.w(Util.LOG_TAG, "connection error");
                try {
                    fullDoc = Jsoup.connect(BASE_URL + INFO_URL[k]).get();
                } catch (IOException e2) {
                    Log.w(Util.LOG_TAG, "connection error");
                    // Give up after three times
                    return null;
                }
            }
        }

        String bQuery = "#" + dayOfWeek + " ~ div div :contains(Breakfast) li";
        String lQuery = "#" + dayOfWeek + " ~ div div :contains(Lunch) li";
        String dQuery = "#" + dayOfWeek + " ~ div div :contains(Dinner) li";

        Log.v(Util.LOG_TAG, "Making Queries");
        breakfastFoodNames = fullDoc.select(bQuery);
        lunchFoodNames = fullDoc.select(lQuery);
        dinnerFoodNames = fullDoc.select(dQuery);
        Log.v(Util.LOG_TAG, "Queries Complete!!!!!");

        ArrayList<MenuItem> breakfastList = new ArrayList<MenuItem>(),
                lunchList = new ArrayList<MenuItem>(),
                dinnerList = new ArrayList<MenuItem>();

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

            fullMenuObj.get(k).setBreakfast(breakfastList);
            fullMenuObj.get(k).setLunch(lunchList);
            fullMenuObj.get(k).setDinner(dinnerList);
            if (fullMenuObj.get(k).getBreakfast().isEmpty() &&
                    (!(fullMenuObj.get(k).getLunch().isEmpty()) ||
                            !(fullMenuObj.get(k).getDinner().isEmpty()))) {
                ArrayList<MenuItem> breakfastMessage = new ArrayList<MenuItem>();
                breakfastMessage.add(new MenuItem(Util.brunchMessage, "-1"));
                fullMenuObj.get(k).setBreakfast(breakfastMessage);
            }
//            return Util.GETLIST_SUCCESS;

        return breakfastList;
    }
}

    /**

    /**
//     * Puts downloaded data from specified date (instead of today) into the full menu object.
//     */
//    public static int getMealList(int month, int day, int year) {
//        for (int i = 0; i < 5; i++) {
//            int res = getSingleMealList(i, month, day, year);
//            /*
//             * For some stupid reason, it throws these stupid unexpected status line errors half the
//             * time on mobile data. So we have to intercept those somehow. getsinglemeallist returns
//             * okhttp failure if it gets one - and getsinglemeallist also tries multiple times
//             * before returning the error. It will only try once for lost internet connection,
//             * though.
//             */
//            if (res == Util.GETLIST_OKHTTP_FAILURE) {
//                res = getSingleMealList(i, month, day, year);
//                if (res == Util.GETLIST_OKHTTP_FAILURE) {
//                    res = getSingleMealList(i, month, day, year);
//                    if (res == Util.GETLIST_OKHTTP_FAILURE) {
//                        res = getSingleMealList(i, month, day, year);
//                        if (res == Util.GETLIST_OKHTTP_FAILURE) {
//                            return Util.GETLIST_INTERNET_FAILURE;
//                        }
//                    }
//                }
//            } else if (res != Util.GETLIST_SUCCESS) {
//                return res;
//            }
//        }
//        return Util.GETLIST_SUCCESS;
//    }
//}
