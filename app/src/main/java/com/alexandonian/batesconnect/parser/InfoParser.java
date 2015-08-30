package com.alexandonian.batesconnect.parser;


import android.util.Log;

import com.alexandonian.batesconnect.activities.MainActivity;
import com.alexandonian.batesconnect.infoItems.EventItem;
import com.alexandonian.batesconnect.infoItems.Menu;
import com.alexandonian.batesconnect.infoItems.MenuItem;
import com.alexandonian.batesconnect.util.Util;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses the incoming file.
 * <p/>
 * <p>Data is stored in the static fullMenu arraylist of
 * Menu objects.
 * <p/>
 * <p>Released under GNU GPL v2 - see doc/LICENCES.txt for more info.
 */

public class InfoParser {

    public static final String BATES_BASE_URL = "http://www.bates.edu";

    public static final String[] EVENTS_URL = {
            "https://events.bates.edu/MasterCalendar/RSSFeeds" +
                    ".aspx?data=exfsp9BbGwOa3zSOgl8KgEgYuxIc0yl1Kf1WvIle1s0%3d",
            "https://events.bates.edu/MasterCalendar/RSSFeeds" +
                    ".aspx?data=rWVImWG4wi1iC5u2UcXAOb8FAG2a07ELl0dalOR1a1E%3d",
            "https://events.bates.edu/MasterCalendar/RSSFeeds" +
                    ".aspx?data=hhAbVFpDFO7OxcTcYlM9LqoreXryCsIt",
            "https://events.bates.edu/MasterCalendar/RSSFeeds" +
                    ".aspx?data=HwqQnFd0XZw2ELTJ3w4YjTzo%2fo2OuzFkwDN7%2bEDRtTQ%3d",
            "https://events.bates.edu/MasterCalendar/RSSFeeds" +
                    ".aspx?data=OiNeXA6LJItp%2bLkkMsbi4wEeKjDIEyUQtbeKsA7Mwd0%3d"
    };

    public static final String[] INFO_URL = {
            "/dining/menu/",
            "/events/",
            "/access/building-hours/"
    };

    public static final String[] BUILDING_HOURS_URL = {
            "semester-building-hours/",
            "break-building-hours/",
            "between-semester-building-hours/",
            "summer-hours/"
    };

    public static boolean manualRefresh = false;
    private static boolean isBrunch;

    public static ArrayList<Menu> fullMenuObj = new ArrayList<Menu>() {{
        add(new Menu());
    }};

    public static ArrayList<ArrayList<EventItem>> EVENTS = new ArrayList<ArrayList<EventItem>>() {{
        add(new ArrayList<EventItem>());
        add(new ArrayList<EventItem>());
        add(new ArrayList<EventItem>());
        add(new ArrayList<EventItem>());
        add(new ArrayList<EventItem>());
    }};

    public static int getSingleMealList(int month, int day, int year) {

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

        List<EventItem> mAllEvents = new ArrayList<>();

        try {
            fullDoc = Jsoup.connect(BATES_BASE_URL + INFO_URL[0]).get();
        } catch (UnknownHostException e) {
            // Internet connection completely missing is a separate error from okhttp
            Log.v(Util.LOG_TAG, "Internet connection missing");
            e.printStackTrace();
            return Util.GETLIST_INTERNET_FAILURE;
        } catch (IOException e) {
            Log.w(Util.LOG_TAG, "connection error");
            try {
                fullDoc = Jsoup.connect(BATES_BASE_URL + INFO_URL[0]).get();
            } catch (IOException e1) {
                Log.w(Util.LOG_TAG, "connection error");
                try {
                    fullDoc = Jsoup.connect(BATES_BASE_URL + INFO_URL[0]).get();
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

        if (MainActivity.isBrunch() && meals.size() == 2) {

            brunchFoodStationNames = meals.get(0).select("div p");
            brunchFoodStations = meals.get(0).select("div ul");

            dinnerFoodStationNames = meals.get(1).select("div p");
            dinnerFoodStations = meals.get(1).select("div ul");

        } else if (meals.size() == 3) {
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
                    if (!breakfastFoodStations.get(i).select("li").get(j).text().equals("")) {
                        breakfastList.add(new MenuItem(breakfastFoodStations.get(i)
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

//        try {
//            RssReader rssReader = new RssReader("https://events.bates
// .edu/MasterCalendar/RSSFeeds" +
//                    ".aspx?data=exfsp9BbGwOa3zSOgl8KgEgYuxIc0yl1Kf1WvIle1s0%3d");
//
//            for (int i = 0; i < rssReader.getItems().size(); i++) {
//                System.out.println(rssReader.getItems().get(i).getTitle());
//                System.out.println(rssReader.getItems().get(i).getPubDate());
//                System.out.println(rssReader.getItems().get(i).getDescription());
//                System.out.println(rssReader.getItems().get(i).getLink());
//                mAllEvents = rssReader.getItems();
//            }
//        } catch (Exception e) {
//            Log.e(Util.LOG_TAG, e.getMessage());
//        }

        fullMenuObj.get(0).setBreakfast(breakfastList);
        fullMenuObj.get(0).setLunch(lunchList);
        fullMenuObj.get(0).setDinner(dinnerList);
        fullMenuObj.get(0).setBrunch(brunchList);

//        fullMenuObj.get(info).setAllEvents(mAllEvents);
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

        int res = getSingleMealList(month, day, year);
            /*
             * For some stupid reason, it throws these stupid unexpected status line errors half the
             * time on mobile data. So we have to intercept those somehow. getsinglemeallist returns
             * okhttp failure if it gets one - and getsinglemeallist also tries multiple times
             * before returning the error. It will only try once for lost internet connection,
             * though.
             */
        if (res == Util.GETLIST_OKHTTP_FAILURE) {
            res = getSingleMealList(month, day, year);
            if (res == Util.GETLIST_OKHTTP_FAILURE) {
                res = getSingleMealList(month, day, year);
                if (res == Util.GETLIST_OKHTTP_FAILURE) {
                    res = getSingleMealList(month, day, year);
                    if (res == Util.GETLIST_OKHTTP_FAILURE) {
                        return Util.GETLIST_INTERNET_FAILURE;
                    }
                }
            }
        } else if (res != Util.GETLIST_SUCCESS) {
            return res;
        }

        return Util.GETLIST_SUCCESS;
    }

    public static int getEvents() {

        ArrayList<Document> eventDocs = new ArrayList<>();
        Document xmlEventsDoc = null;

        try {

            for (int i = 0; i < 5; i++) {
                eventDocs.add(Jsoup.parse(Jsoup.connect(EVENTS_URL[i]).get().toString(), "",
                        Parser.xmlParser()));
            }
        } catch (UnknownHostException e) {
        } catch (IOException e2) {
        }

//        for (Element e : xmlEventsDoc.select("title")) {
//            mAllEvents.add(new EventItem(e.text().replace("<![CDATA[", "").replace
//                    ("]]>", ""), 200,
////                        rssReader.getItems().get(i).getDescription()));
//                    System.out.println(e.text().replace("<![CDATA[", "").replace("]]>", ""));
//
//        }

        for (int i = 0; i < eventDocs.size(); i++) {
            for (int j = 1; j < eventDocs.get(i).select("title").size(); j++) {
                EVENTS.get(i).add(new EventItem(eventDocs.get(i).select("title").get(j)
                        .text().replace("<![CDATA[", "").replace("]]>", ""),
                        eventDocs.get(i).select("pubdate").get(j).text().substring(0,
                                eventDocs.get(i).select("pubdate").get(j).text().length() - 12),
                        Util.getEventDrawable(eventDocs.get(i).select("category").get(j).text()),
                        Util.EVENT_CELL_HEIGHT,
                        eventDocs.get(i).select("description").get(j).text()));

            }
        }


//        try {
//            RssReader rssReader = new RssReader("https://events.bates
// .edu/MasterCalendar/RSSFeeds" +
//                    ".aspx?data=exfsp9BbGwOa3zSOgl8KgEgYuxIc0yl1Kf1WvIle1s0%3d");
//
//            for (int i = 0; i < rssReader.getItems().size(); i++) {
//                Log.v(Util.LOG_TAG, rssReader.getItems().get(i).getTitle());
//                Log.v(Util.LOG_TAG, rssReader.getItems().get(i).getPubDate());
//                Log.v(Util.LOG_TAG, rssReader.getItems().get(i).getDescription());
//                Log.v(Util.LOG_TAG, rssReader.getItems().get(i).getLink());
//
//
//                mAllEvents.add(new EventItem(rssReader.getItems().get(i).getTitle(), 200,
//                        rssReader.getItems().get(i).getDescription()));
//
//            }
//        } catch (Exception e) {
//            Log.e(Util.LOG_TAG, e.getMessage());
//        }
//
        if (EVENTS != null) { return Util.GETLIST_SUCCESS; } else { return 0; }

    }
}
