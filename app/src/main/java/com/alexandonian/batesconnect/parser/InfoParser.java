package com.alexandonian.batesconnect.parser;


import android.util.Log;

import com.alexandonian.batesconnect.activities.MainActivity;
import com.alexandonian.batesconnect.util.CollegeMenu;
import com.alexandonian.batesconnect.util.MenuItem;
import com.alexandonian.batesconnect.util.Util;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Parses the incoming file.
 * <p>
 * <p>Data is stored in the static fullMenu arraylist of
 * CollegeMenu objects.
 * <p>
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
    private static boolean isBrunch;

    public static ArrayList<CollegeMenu> fullMenuObj = new ArrayList<CollegeMenu>() {{
        add(new CollegeMenu());
        add(new CollegeMenu());
        add(new CollegeMenu());

    }};

    public static int getSingleMealList(int info, int month, int day, int year) {

        String dayOfWeek = Util.getDayOfWeek(month, day, year);


//        TO BE USED IF URI BUILDER IS NECESSARY
//        Uri builtUri = Uri.parse(BATES_BASE_URL).buildUpon()
//                .appendQueryParameter("INFO_PARAM",INFO_URL[info])
//                .build();


        Document fullDoc;
        Elements meals = null,
                breafast = null,
                lunchFoodNames = null,
                dinnerFoodNames = null,
                brunchFoodNames = null;
        Elements mealNames = null,
                breakfastFoodStationNames = null,
                lunchFoodStationNames = null,
                dinnerFoodStationNames = null,
                brunchFoodStationNames = null,
                breakfastFoodStations = null,
                lunchFoodStations = null,
                dinnerFoodStations = null,
                brunchFoodStations = null;

        ArrayList<MenuItem> breakfastList = new ArrayList<MenuItem>(),
                lunchList = new ArrayList<MenuItem>(),
                dinnerList = new ArrayList<MenuItem>(),
                brunchList = new ArrayList<MenuItem>();

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

//        Log.v(Util.LOG_TAG, "Making Queries");
        meals = fullDoc.select(CSSQuesry);
        mealNames = meals.select("h2");

        if (MainActivity.isBrunch()) {

        brunchFoodStationNames = meals.get(0).select("div p");
        brunchFoodStations = meals.get(0).select("div ul");

        dinnerFoodStationNames = meals.get(1).select("div p");
        dinnerFoodStations = meals.get(1).select("div ul");

        } else {
        breakfastFoodStationNames = meals.get(0).select("div p");
        breakfastFoodStations = meals.get(0).select("div ul");

        lunchFoodStationNames = meals.get(1).select("div p");
        lunchFoodStations = meals.get(1).select("div ul");

        dinnerFoodStationNames = meals.get(2).select("div p");
        dinnerFoodStations = meals.get(2).select("div ul");
        }


        if (breakfastFoodStationNames != null && breakfastFoodStations != null) {
            for (int i = 0; i < breakfastFoodStationNames.size(); i++) {
                breakfastList.add(new MenuItem(breakfastFoodStationNames.get(i)
                        .text(), MenuItem.SECTION));
                for (int j = 0; j < breakfastFoodStations.get(i).select("li").size(); j++) {
                    if (!breakfastFoodStations.get(i).select("li").get(j).text().equals("")) {                        breakfastList.add(new MenuItem(breakfastFoodStations.get(i)
                                .select("li").get(j).text(), MenuItem.ITEM));
                    }
                }
            }
        }

        if (lunchFoodStationNames != null && lunchFoodStations != null) {
            for (int i = 0; i < lunchFoodStationNames.size(); i++) {
                lunchList.add(new MenuItem(lunchFoodStationNames.get(i)
                        .text(), MenuItem.SECTION));
                for (int j = 0; j < lunchFoodStations.get(i).select("li").size(); j++) {
                    if (!lunchFoodStations.get(i).select("li").get(j).text().equals("")) {
                        lunchList.add(new MenuItem(lunchFoodStations.get(i).select
                                ("li").get(j).text(), MenuItem.ITEM));
                    }
                }
            }
        }

        if (dinnerFoodStationNames != null && dinnerFoodStations != null) {
            for (int i = 0; i < dinnerFoodStationNames.size(); i++) {
                dinnerList.add(new MenuItem(dinnerFoodStationNames.get(i)
                        .text(), MenuItem.SECTION));
                for (int j = 0; j < dinnerFoodStations.get(i).select("li").size(); j++) {
                    if (!dinnerFoodStations.get(i).select("li").get(j).text().equals("")) {
                        dinnerList.add(new MenuItem(dinnerFoodStations.get(i).select
                                ("li").get(j).text(), MenuItem.ITEM));
                    }
                }
            }
        }

        if (brunchFoodStationNames != null && brunchFoodStations != null) {
            for (int i = 0; i < brunchFoodStationNames.size(); i++) {
                brunchList.add(new MenuItem(brunchFoodStationNames.get(i)
                        .text(), MenuItem.SECTION));
                for (int j = 0; j < brunchFoodStations.get(i).select("li").size(); j++) {
                    if (!brunchFoodStations.get(i).select("li").get(j).text().equals("")) {
                        brunchList.add(new MenuItem(brunchFoodStations.get(i).select
                                ("li").get(j).text(), MenuItem.ITEM));
                    }
                }
            }
        }

        fullMenuObj.get(info).setBreakfast(breakfastList);
        fullMenuObj.get(info).setLunch(lunchList);
        fullMenuObj.get(info).setDinner(dinnerList);
        fullMenuObj.get(info).setBrunch(brunchList);
//        if (fullMenuObj.get(info).getBreakfast().isEmpty() &&
//                (!(fullMenuObj.get(info).getLunch().isEmpty()) ||
//                        !(fullMenuObj.get(info).getDinner().isEmpty()))) {
//            ArrayList<MenuItem> breakfastMessage = new ArrayList<MenuItem>();
//            breakfastMessage.add(new MenuItem(Util.brunchMessage));
//            fullMenuObj.get(info).setBreakfast(breakfastMessage);
//        }
        return Util.GETLIST_SUCCESS;

    }


    /**
     * /**
     * //     * Puts downloaded data from specified date (instead of today) into the full menu
     * object.
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
