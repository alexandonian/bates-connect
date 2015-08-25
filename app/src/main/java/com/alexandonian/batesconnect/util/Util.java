package com.alexandonian.batesconnect.util;

import android.util.Log;
import android.widget.Toast;

import com.alexandonian.batesconnect.R;
import com.alexandonian.batesconnect.activities.MainActivity;
import com.alexandonian.batesconnect.parser.InfoParser;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Class for holding static functions and enums such as getting today, getting current meal, etc
 * that were previously scattered around various classes.
 *
 * <p>Released under GNU GPL v2 - see doc/LICENCES.txt for more info.
 *
 */
public class Util {

    public static final String BATES_BASE_URL = "http://www.bates.edu";

    // Times that the app decides to switch over to the specified meal.
    public static final int DINNER_SWITCH_TIME = 15, // 3 PM
            LUNCH_SWITCH_TIME = 11, // 11 AM
            BREAKFAST_SWITCH_TIME = 21; // 9 PM

    // Enums for meal constants. They match the indexes in the meals array.
    public static final int BREAKFAST = 0,
            LUNCH = 1,
            DINNER = 2;

    public static final String[] meals = {
            "Breakfast",
            "Lunch",
            "Dinner"
    };

    public static final String[] collegeList = {
            "Cowell/Stevenson",
            "Crown/Merrill",
            "Porter/Kresge",
            "Eight/Oakes",
            "Nine/Ten"
    };

    // Enums for returns of data fetching status
    public static final int GETLIST_SUCCESS = 1,
            GETLIST_INTERNET_FAILURE = 0,
            GETLIST_DATABASE_FAILURE = -1,
            GETLIST_OKHTTP_FAILURE = 2;

    // Extra word key for intent creation
    public static final String EXTRA_WORD = "com.nickivy.ucscdining.widget.WORD";

    public static final String LOG_TAG = "batesconnect";

    public static final String WIDGETSTATE_PREFS = "widgetstate_prefs";

    public static final String brunchMessage = "See lunch for today\'s brunch menu";

    // Tags for intent passed to menu_main activity from widget
    public static final String TAG_COLLEGE = "tag_college",
            TAG_MEAL = "tag_meal",
            TAG_MONTH = "tag_month",
            TAG_DAY = "tag_day",
            TAG_YEAR = "tag_year",
            TAG_URL = "tag_url",
            TAG_USESAVED = "tag_usesaved";

    public static final int NO_BACKUP_COLLEGE = 6;

    public static final String NO_INTERNET = "No Internet Connection";

    public static final String DATABASE_FAILURE = "Database Failure";

    public static final String SOMETHING_WRONG = "Something Went Wrong";

    public static final int TOAST_LENGTH = Toast.LENGTH_SHORT;

    public static final String TIME_ZONE = "EST";

    public static boolean isBrunch = MainActivity.isBrunch();

    /**
     * Returns today's date as a 3-number int array. [month, day, year]
     *
     * <p>Does not necessarily return 'today' but instead returns the day the app should display.
     * This happens after the dining hall closes.
     */
    public static int[] getToday() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone(Util.TIME_ZONE));
        // If time is past dining hall closing (which is the breakfast switch time) return tomorrow
        if (cal.get(Calendar.HOUR_OF_DAY) >= BREAKFAST_SWITCH_TIME) {
            cal.add(Calendar.DATE, 1);
        }

        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);

        int ret[] = {month, day, year};
        return ret;
    }

    /**
     * Gets meal based on time of day, based on times at top of file.
     * Will not return breakfast if brunch message is present
     *
     * @return return values are also enumerated at top of file
     */
    public static int getCurrentMeal(int college) {
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.HOUR_OF_DAY) >= DINNER_SWITCH_TIME &&
                cal.get(Calendar.HOUR_OF_DAY) < BREAKFAST_SWITCH_TIME) {
            return DINNER;
        }
        if (cal.get(Calendar.HOUR_OF_DAY) >= LUNCH_SWITCH_TIME &&
                cal.get(Calendar.HOUR_OF_DAY) < DINNER_SWITCH_TIME) {
            return LUNCH;
        }
        if(cal.get(Calendar.HOUR_OF_DAY) >= BREAKFAST_SWITCH_TIME ||
                cal.get(Calendar.HOUR_OF_DAY) < LUNCH_SWITCH_TIME) {
            if (InfoParser.fullMenuObj.get(college).getBreakfast().size() > 0) {
                if (InfoParser.fullMenuObj.get(college).getBreakfast().get(0).getItemName()
                        .equals(brunchMessage)) {
                    return LUNCH;
                }
            }
            return BREAKFAST;
        }
        return -1;
    }

    public static String getDayOfWeek(int month, int day, int year) {

        String[] weekDays = {
                "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Calendar calendar = new GregorianCalendar(year, month - 1, day);
        calendar.setTimeZone(TimeZone.getTimeZone(Util.TIME_ZONE));
        String dayOfWeek = weekDays[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        return dayOfWeek;
    }

    public static String getMonthName(int month){
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August",
                "September", "October", "November", "December"};
        return months[month - 1];
    }
    public static boolean isBrunch(){

        int[] date = MainActivity.getRequestedDate();

        if (date !=null) {
            String dayOfWeek = getDayOfWeek(date[0], date[1], date[2]);
            return (dayOfWeek.equals("Saturday") || dayOfWeek.equals("Sunday"));
        } else {
             date = Util.getToday();
            String dayOfWeek = getDayOfWeek(date[0], date[1], date[2]);
            return (dayOfWeek.equals("Saturday") || dayOfWeek.equals("Sunday"));
        }
    }

    public static int getEventDrawable(String category) {
        switch (category) {
            case "Drawing":
                return R.drawable.art_palette;
            case "Athletic Game":
                return R.drawable.ball_football2;
            case "Athletic Event":
                return R.drawable.ball_football2;
            case "Reading":
                return R.drawable.book;
            case "Presentation":
                return R.drawable.presentation_icon;
            case "Concert":
                return R.drawable.music_beamed_note;
            case "Dance":
                return R.drawable.dance_icon;
            case "Symposium":
                return R.drawable.presentation_icon;
            default:
                return R.drawable.bates_connect_icon;
        }
    }

    public static boolean isGluten(String item) {
        return item.contains("Gluten") | item.contains("gluten");
    }

    public static boolean isVegan(String item) {
        if (item.matches("Vegan Bar")){
            return false;
        }
        return item.contains("Vegan") | item.contains("vegan");
    }
}