package com.alexandonian.batesconnect.util;

import android.util.Log;

import com.alexandonian.batesconnect.infoItems.Menu;
import com.alexandonian.batesconnect.infoItems.MenuItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Administrator on 7/26/2015.
 */
public class test {

    public static void main(String[] args) {

        String item = "Make Your Own Sundae";

        int test = 1;

        String test1 = item + test;

        System.out.println(test1);

        getSingleMealList(1, 8, 6, 2015);

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

    public static ArrayList<Menu> fullMenuObj = new ArrayList<Menu>() {{
        add(new Menu());
        add(new Menu());
        add(new Menu());

    }};

    public static int getSingleMealList(int info, int month, int day, int year) {

//        String dayOfWeek = Util.getDayOfWeek(month, day, year);
        String dayOfWeek = "Wednesday";


//        TO BE USED IF URI BUILDER IS NECESSARY
//        Uri builtUri = Uri.parse(BATES_BASE_URL).buildUpon()
//                .appendQueryParameter("INFO_PARAM",INFO_URL[info])
//                .build();



        Document fullDoc;
        Elements meals = null,
                events = null,

                breakfastDeliBar = null,
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

        Elements eventsByDay = null;

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

        Document xmlEventsDoc = null;
        try {
            String xml = Jsoup.connect("https://events.bates.edu/MasterCalendar/RSSFeeds" +
                    ".aspx?data=exfsp9BbGwOa3zSOgl8KgEgYuxIc0yl1Kf1WvIle1s0%3d").get().toString();
            xmlEventsDoc = Jsoup.parse(xml, "", Parser.xmlParser());

        } catch (UnknownHostException e) {
        } catch (IOException e2) {
        }

//        for (Element e : xmlEventsDoc.select("title")) {
//            System.out.println(e.text()
//        }

        for (int i = 0; i < xmlEventsDoc.select("title").size(); i++) {
            System.out.println(xmlEventsDoc.select("title").get(i).text().replace("<![CDATA[", "").replace("]]>", ""));
            System.out.println(xmlEventsDoc.select("pubdate").get(i).text());
            System.out.println(xmlEventsDoc.select("description").get(i).text());
            System.out.println(xmlEventsDoc.select("link").get(i).text());
        }


        String item = "Make Your Own Sundae";

        int test = 1;

        String test1 = item + test;

        System.out.println(test1);


        if (Util.isGluten(item))
            System.out.println("works!");

//        try {
//            RssReader rssReader = new RssReader("https://events.bates.edu/MasterCalendar/RSSFeeds" +
//                    ".aspx?data=exfsp9BbGwOa3zSOgl8KgEgYuxIc0yl1Kf1WvIle1s0%3d");
//
//            for (int i = 0; i < rssReader.getItems().size(); i++) {
//                System.out.println(rssReader.getItems().get(i).getTitle());
//                System.out.println(rssReader.getItems().get(i).getPubDate());
//                System.out.println(rssReader.getItems().get(i).getDescription());
//                System.out.println(rssReader.getItems().get(i).getLink());
//            }
//        } catch (Exception e) {
//            Log.e(Util.LOG_TAG, e.getMessage());
//        }
//        String bQuery = "#" + dayOfWeek + " ~ div div :contains(Breakfast) li";
//        String lQuery = "#" + dayOfWeek + " ~ div div :contains(Lunch) li";
//        String dQuery = "#" + dayOfWeek + " ~ div div :contains(Dinner) li";

//        String CSSQuesry = "#" + dayOfWeek + " ~ div .meal-wrap";
//
////        Log.v(Util.LOG_TAG, "Making Queries");
//        meals = fullDoc.select(CSSQuesry);
//        mealNames = meals.select("h2");
//
//        if (MainActivity.isBrunch()) {
//
//            brunchFoodStationNames = meals.get(0).select("div p");
//            brunchFoodStations = meals.get(0).select("div ul");
//
//            dinnerFoodStationNames = meals.get(1).select("div p");
//            dinnerFoodStations = meals.get(1).select("div ul");
//
//        } else {
//            breakfastFoodStationNames = meals.get(0).select("div p");
//            breakfastFoodStations = meals.get(0).select("div ul");
//
//            lunchFoodStationNames = meals.get(1).select("div p");
//            lunchFoodStations = meals.get(1).select("div ul");
//
//            dinnerFoodStationNames = meals.get(2).select("div p");
//            dinnerFoodStations = meals.get(2).select("div ul");
//        }
//
//
//        if (breakfastFoodStationNames != null && breakfastFoodStations != null) {
//            for (int i = 0; i < breakfastFoodStationNames.size(); i++) {
//                breakfastList.add(new MenuItem(MenuItem.SECTION, breakfastFoodStationNames.get(i)
//                        .text()));
//                for (int j = 0; j < breakfastFoodStations.get(i).select("li").size(); j++) {
//                    breakfastList.add(new MenuItem(MenuItem.ITEM, breakfastFoodStations.get(i)
//                            .select("li").get(j).text()));
//                }
//            }
//        }
//
//        if (lunchFoodStationNames != null && lunchFoodStations != null) {
//            for (int i = 0; i < lunchFoodStationNames.size(); i++) {
//                lunchList.add(new MenuItem(MenuItem.SECTION, lunchFoodStationNames.get(i)
//                        .text()));
//                for (int j = 0; j < lunchFoodStations.get(i).select("li").size(); j++) {
//                    lunchList.add(new MenuItem(MenuItem.ITEM, lunchFoodStations.get(i).select
//                            ("li").get(j).text()));
//                }
//            }
//        }
//
//        if (dinnerFoodStationNames != null && dinnerFoodStations != null) {
//            for (int i = 0; i < dinnerFoodStationNames.size(); i++) {
//                dinnerList.add(new MenuItem(MenuItem.SECTION, dinnerFoodStationNames.get(i)
//                        .text()));
//                for (int j = 0; j < dinnerFoodStations.get(i).select("li").size(); j++) {
//                    dinnerList.add(new MenuItem(MenuItem.ITEM, dinnerFoodStations.get(i).select
//                            ("li").get(j).text()));
//                }
//            }
//        }
//
//        if (brunchFoodStationNames != null && brunchFoodStations != null) {
//            for (int i = 0; i < brunchFoodStationNames.size(); i++) {
//                brunchList.add(new MenuItem(MenuItem.SECTION, brunchFoodStationNames.get(i)
//                        .text()));
//                for (int j = 0; j < brunchFoodStations.get(i).select("li").size(); j++) {
//                    brunchList.add(new MenuItem(MenuItem.ITEM, brunchFoodStations.get(i).select
//                            ("li").get(j).text()));
//                }
//            }
//        }
//
//        for (int i = 0; i < breakfastFoodStationNames.size(); i++) {
//            System.out.println(breakfastFoodStationNames.get(i).text());
//            System.out.println
// ("-----------------------------------------------------------------");
//            for (int j = 0; j < breakfastFoodStations.get(i).select("li").size(); j++) {
//                System.out.println(breakfastFoodStations.get(i).select("li").get(j).text());
//            }
//            System.out.println("");
//        }
//
//        System.out.print("++++++++++++++++++++++ ");
//        System.out.print(mealNames.get(1).text());
//        System.out.println("++++++++++++++++++++++");
//        for (int i = 0; i < lunchFoodStationNames.size(); i++) {
//            System.out.println(lunchFoodStationNames.get(i).text());
//            System.out.println
// ("-----------------------------------------------------------------");
//            for (int j = 0; j < lunchFoodStations.get(i).select("li").size(); j++) {
//                System.out.println(lunchFoodStations.get(i).select("li").get(j).text());
//            }
//            System.out.println("");
//        }
//
//        System.out.print("++++++++++++++++++++++ ");
//        System.out.print(mealNames.get(2).text());
//        System.out.println("++++++++++++++++++++++");
//        for (int i = 0; i < dinnerFoodStationNames.size(); i++) {
//            System.out.println(dinnerFoodStationNames.get(i).text());
//            System.out.println
// ("-----------------------------------------------------------------");
//            for (int j = 0; j < dinnerFoodStations.get(i).select("li").size(); j++) {
//                System.out.println(dinnerFoodStations.get(i).select("li").get(j).text());
//            }
//            System.out.println("");
//        }

        return 1;
    }
}
