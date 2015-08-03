package com.alexandonian.batesconnect.util;

import android.util.Log;

import com.alexandonian.batesconnect.MainActivity;
import com.alexandonian.batesconnect.data.InfoContract;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Administrator on 7/26/2015.
 */
public class test {

    public static void main(String[] args) {

        getSingleMealList(0, 8, 3, 2015);

    }

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

    public static ArrayList<CollegeMenu> fullMenuObj = new ArrayList<CollegeMenu>() {{
        add(new CollegeMenu());
        add(new CollegeMenu());
        add(new CollegeMenu());

    }};

    public static int getSingleMealList(int info, int month, int day, int year) {

//        String dayOfWeek = Util.getDayOfWeek(month, day, year);
        String dayOfWeek = "Monday";


//        TO BE USED IF URI BUILDER IS NECESSARY
//        Uri builtUri = Uri.parse(BATES_BASE_URL).buildUpon()
//                .appendQueryParameter("INFO_PARAM",INFO_URL[info])
//                .build();


        Document fullDoc;
        Elements foodNames = null,
                breafast = null,
                breakfastStations = null,
                breakfastDeliBar = null,
                lunchFoodNames = null,
                dinnerFoodNames = null,
                brunchFoodNames = null;
        Elements breakfastFoodStations = null;

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
        foodNames = fullDoc.select(CSSQuesry);
        breakfastFoodStations = foodNames.get(0).select("div ul");
        breakfastDeliBar = breakfastFoodStations.get(0).select("li");


        for (int i = 0; i < breakfastDeliBar.size(); i++) {
            System.out.println(breakfastDeliBar.get(i).text());
        }





        return 1;
    }
}
