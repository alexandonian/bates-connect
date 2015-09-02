package com.alexandonian.batesconnect.parser;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alexandonian.batesconnect.activities.MainActivity;
import com.alexandonian.batesconnect.database.InfoContract;
import com.alexandonian.batesconnect.database.InfoDbHelper;
import com.alexandonian.batesconnect.infoItems.EventItem;
import com.alexandonian.batesconnect.infoItems.MenuItem;
import com.alexandonian.batesconnect.util.Util;

import java.util.ArrayList;

/**
 * Created by Administrator on 7/21/2015.
 */
public class InfoDataFetcher {

    public static int fetchMenu(Context context, int month, int day, int year) {

        InfoDbHelper menuStore = new InfoDbHelper(context);
        SQLiteDatabase db;

        if (menuStore == null) {
            return Util.GETLIST_DATABASE_FAILURE;
        }

        db = menuStore.getReadableDatabase();

        String[] projection = {
                InfoContract.COLUMN_MONTH,
                InfoContract.COLUMN_DAY,
                InfoContract.COLUMN_YEAR
        };

        String selection = InfoContract.COLUMN_MONTH + " = ? AND " +
                InfoContract.COLUMN_DAY + " = ? AND " +
                InfoContract.COLUMN_YEAR + " = ?";
        String[] selectionArgs = new String[3];

        selectionArgs[0] = "" + month;
        selectionArgs[1] = "" + day;
        selectionArgs[2] = "" + year;

        Cursor cursor = db.query(InfoContract.TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);
        cursor.moveToFirst();

        boolean cursorDNE = (cursor.getCount() == 0);
        cursor.close();
        db.close();

        // If data for today does not exist, or a manual refresh is requested,
        // download/store data
        if (cursorDNE || InfoParser.manualRefresh) {
            int result = InfoParser.getInfoList(month, day, year);
            if (result != Util.GETLIST_SUCCESS) {
                return result;
            }


            // Write to the database

            db = menuStore.getWritableDatabase();

            int today[] = Util.getToday();

            if (InfoParser.manualRefresh) {
                db.delete(InfoContract.TABLE_NAME, null, null);
            } else {
                db.delete(InfoContract.TABLE_NAME,
                        InfoContract.COLUMN_MONTH + "<? AND " + InfoContract.COLUMN_YEAR + " =?",
                        new String[]{"" + today[0], "" + today[2]});

                db.delete(InfoContract.TABLE_NAME,
                        InfoContract.COLUMN_YEAR + " <?",
                        new String[]{"" + today[2]});

                db.delete(InfoContract.TABLE_NAME,
                        InfoContract.COLUMN_MONTH + "=? AND " + InfoContract.COLUMN_DAY + "<? AND" +
                                " " +
                                InfoContract.COLUMN_YEAR + " =?",
                        new String[]{"" + today[0], "" + today[1], "" + today[2]});
            }

            // Begin Writing Data

            SQLiteStatement statement = db.compileStatement("INSERT INTO " +
                    InfoContract.TABLE_NAME + "(" + InfoContract.COLUMN_INFO + ", " +
                    InfoContract.COLUMN_MEAL + ", " + InfoContract.COLUMN_MENUITEM + ", " +
                    InfoContract.COLUMN_TYPE + ", " + InfoContract.COLUMN_MONTH + ", " +
                    InfoContract.COLUMN_DAY + ", " +
                    InfoContract.COLUMN_YEAR + ") VALUES (?,?,?,?,?,?,?);");

            // Using SQLite statement keeps the database 'open' and apparently is a bit faster.
            db.beginTransaction();

            if (MainActivity.isBrunch()) {

                // If it is brunch, write Brunch

                for (int j = 0; j < 1; j++) {
                    statement.clearBindings();
                    for (int i = 0; i < InfoParser.fullMenuObj.get(j).getBrunch().size(); i++) {
                        statement.bindLong(1, j);
                        statement.bindLong(2, 3); // 0 for brunch
                        statement.bindString(3, InfoParser.fullMenuObj.get(j).getBrunch().get(i)
                                .getItemName());
                        statement.bindLong(4, InfoParser.fullMenuObj.get(j).getBrunch().get(i)
                                .getItemType());
                        statement.bindLong(5, month);
                        statement.bindLong(6, day);
                        statement.bindLong(7, year);
                        try {
                            statement.execute();
                        } catch (SQLiteConstraintException e) {
                            return Util.GETLIST_DATABASE_FAILURE;
                        }
                    }
                }

            } else {

                // Otherwise write breakfast and lunch
                for (int j = 0; j < 1; j++) {
                    statement.clearBindings();
                    for (int i = 0; i < InfoParser.fullMenuObj.get(j).getBreakfast().size(); i++) {
                        statement.bindLong(1, j); // Nav Drawer Item
                        statement.bindLong(2, 0); // 0 for breakfast
                        statement.bindString(3, InfoParser.fullMenuObj.get(j).getBreakfast().get(i)
                                .getItemName());
                        statement.bindLong(4, InfoParser.fullMenuObj.get(j).getBreakfast().get(i)
                                .getItemType());
                        statement.bindLong(5, month);
                        statement.bindLong(6, day);
                        statement.bindLong(7, year);
                        try {
                            statement.execute();
                        } catch (SQLiteConstraintException e) {
                            return Util.GETLIST_DATABASE_FAILURE;
                        }
                    }

                    for (int i = 0; i < InfoParser.fullMenuObj.get(j).getLunch().size(); i++) {
                        statement.bindLong(1, j);
                        statement.bindLong(2, 1); // 1 for lunch
                        statement.bindString(3, InfoParser.fullMenuObj.get(j).getLunch().get(i)
                                .getItemName());
                        statement.bindLong(4, InfoParser.fullMenuObj.get(j).getLunch().get(i)
                                .getItemType());
                        statement.bindLong(5, month);
                        statement.bindLong(6, day);
                        statement.bindLong(7, year);
                        try {
                            statement.execute();
                        } catch (SQLiteConstraintException e) {
                            return Util.GETLIST_DATABASE_FAILURE;
                        }
                    }
                }
            }

            // Write Dinner Always
            for (int j = 0; j < 1; j++) {
                for (int i = 0; i < InfoParser.fullMenuObj.get(j).getDinner().size(); i++) {
                    statement.bindLong(1, j);
                    statement.bindLong(2, 2); // 2 for dinner
                    statement.bindString(3, InfoParser.fullMenuObj.get(j).getDinner().get(i)
                            .getItemName());
                    statement.bindLong(4, InfoParser.fullMenuObj.get(j).getDinner().get(i)
                            .getItemType());
                    statement.bindLong(5, month);
                    statement.bindLong(6, day);
                    statement.bindLong(7, year);
                    try {
                        statement.execute();
                    } catch (SQLiteConstraintException e) {
                        return Util.GETLIST_DATABASE_FAILURE;
                    }
                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

        } else {
            // Close db
            db.close();
            db = menuStore.getReadableDatabase();

            ArrayList<MenuItem> breakFastLoaded,
                    lunchLoaded,
                    dinnerLoaded,
                    brunchLoaded;

            String[] mainProjection = {
                    InfoContract.COLUMN_MENUITEM,
                    InfoContract.COLUMN_TYPE,
                    InfoContract.COLUMN_INFO,
                    InfoContract.COLUMN_MONTH,
                    InfoContract.COLUMN_DAY,
                    InfoContract.COLUMN_YEAR
            };

            selection = InfoContract.COLUMN_INFO + "= ? AND " +
                    InfoContract.COLUMN_MEAL + "= ? AND " +
                    InfoContract.COLUMN_MONTH + "= ? AND " +
                    InfoContract.COLUMN_DAY + "= ? AND " +
                    InfoContract.COLUMN_YEAR + "= ?";

            String[] mainSelectionArgs = new String[5];

            // For each of the j infos, load data into the full menu object
            for (int j = 0; j < 1; j++) {
                mainSelectionArgs[0] = "" + j; // Arg dictates which nav drawer item (menu,
                // events..)
                mainSelectionArgs[1] = "" + 0; // This parameter controls which meal
                mainSelectionArgs[2] = "" + month; // Date parameters
                mainSelectionArgs[3] = "" + day;
                mainSelectionArgs[4] = "" + year;

                if (MainActivity.isBrunch()) {

                    // BRUNCH
                    mainSelectionArgs[1] = "" + 3;

                    cursor = db.query(InfoContract.TABLE_NAME,
                            mainProjection, selection, mainSelectionArgs, null, null, null);

                    cursor.moveToFirst();
                    brunchLoaded = new ArrayList<MenuItem>();

                    for (int i = 0; i < cursor.getCount(); i++) {
                        brunchLoaded.add(new MenuItem(cursor.getString
                                (cursor.getColumnIndexOrThrow(InfoContract.COLUMN_MENUITEM)),
                                (int) cursor.getLong(cursor.getColumnIndexOrThrow(InfoContract
                                        .COLUMN_TYPE))));
                        cursor.moveToNext();
                    }
                    InfoParser.fullMenuObj.get(j).setBrunch(brunchLoaded);
                    cursor.close();

                    // Load Dinner
                    mainSelectionArgs[1] = "" + 2;

                    cursor = db.query(InfoContract.TABLE_NAME,
                            mainProjection, selection, mainSelectionArgs, null, null, null);

                    cursor.moveToFirst();
                    dinnerLoaded = new ArrayList<MenuItem>();

                    // DINNER (w/ BRUNCH)
                    for (int i = 0; i < cursor.getCount(); i++) {
                        dinnerLoaded.add(new MenuItem(cursor.getString
                                (cursor.getColumnIndexOrThrow(InfoContract.COLUMN_MENUITEM)),
                                (int) cursor.getLong(cursor.getColumnIndexOrThrow(InfoContract
                                        .COLUMN_TYPE))));
                        cursor.moveToNext();
                    }
                    InfoParser.fullMenuObj.get(j).setDinner(dinnerLoaded);
                    cursor.close();


                } else {

                    // Load Breakfast, Lunch and Dinner
                    cursor = db.query(InfoContract.TABLE_NAME,
                            mainProjection, selection, mainSelectionArgs, null, null, null);

                    cursor.moveToFirst();
                    breakFastLoaded = new ArrayList<MenuItem>();

                    // BREAKFAST
                    for (int i = 0; i < cursor.getCount(); i++) {
                        breakFastLoaded.add(new MenuItem(cursor.getString
                                (cursor.getColumnIndexOrThrow(InfoContract.COLUMN_MENUITEM)),
                                (int) cursor.getLong(cursor.getColumnIndexOrThrow(InfoContract
                                        .COLUMN_TYPE))));
                        cursor.moveToNext();
                    }
                    InfoParser.fullMenuObj.get(j).setBreakfast(breakFastLoaded);
                    cursor.close();

                    mainSelectionArgs[1] = "" + 1;

                    cursor = db.query(InfoContract.TABLE_NAME,
                            mainProjection, selection, mainSelectionArgs, null, null, null);

                    cursor.moveToFirst();
                    lunchLoaded = new ArrayList<MenuItem>();

                    // LUNCH
                    for (int i = 0; i < cursor.getCount(); i++) {
                        lunchLoaded.add(new MenuItem(cursor.getString
                                (cursor.getColumnIndexOrThrow(InfoContract.COLUMN_MENUITEM)),
                                (int) cursor.getLong(cursor.getColumnIndexOrThrow(InfoContract
                                        .COLUMN_TYPE))));
                        cursor.moveToNext();
                    }
                    InfoParser.fullMenuObj.get(j).setLunch(lunchLoaded);
                    cursor.close();

                    mainSelectionArgs[1] = "" + 2;

                    cursor = db.query(InfoContract.TABLE_NAME,
                            mainProjection, selection, mainSelectionArgs, null, null, null);

                    cursor.moveToFirst();
                    dinnerLoaded = new ArrayList<MenuItem>();

                    // DINNER
                    for (int i = 0; i < cursor.getCount(); i++) {
                        dinnerLoaded.add(new MenuItem(cursor.getString
                                (cursor.getColumnIndexOrThrow(InfoContract.COLUMN_MENUITEM)),
                                (int) cursor.getLong(cursor.getColumnIndexOrThrow(InfoContract
                                        .COLUMN_TYPE))));
                        cursor.moveToNext();
                    }
                    InfoParser.fullMenuObj.get(j).setDinner(dinnerLoaded);
                    cursor.close();
                }
                db.close();
                menuStore.close();
            }
        }

        return Util.GETLIST_SUCCESS;
    }

    public static int fetchEvents(Context context) {
        InfoDbHelper eventStore = new InfoDbHelper(context);
        SQLiteDatabase db;

        if (eventStore == null) {
            return Util.GETLIST_DATABASE_FAILURE;
        }

        db = eventStore.getReadableDatabase();

        String[] projection = {
                InfoContract.COLUMN_CATEGORY,
                InfoContract.COLUMN_TITLE,
                InfoContract.COLUMN_PUBDATE,
                InfoContract.COLUMN_IMG_RESOURCE,
                InfoContract.COLUMN_DESCRIPTION
        };

        String selection = InfoContract.COLUMN_CATEGORY + " = ?";
//
        String[] selectionArgs = new String[1];
        selectionArgs[0] = "" + 0;


        Cursor c = db.query(InfoContract.TABLE_NAME_EVENTS, projection, selection,
                selectionArgs, null, null, null);
        c.moveToFirst();

        boolean cursorDNE = (c.getCount() == 0);
        c.close();
        db.close();

        // If data does not exist, or a manual refresh is requested, download and store data
        if (cursorDNE || InfoParser.manualRefresh) {
            for (int i = 0; i < InfoParser.EVENTS.size(); i++) {
                InfoParser.EVENTS.set(i, new ArrayList<EventItem>());
            }
            int result = InfoParser.getEvents();
            if (result != Util.GETLIST_SUCCESS) {
                return result;
            }

            db = eventStore.getWritableDatabase();

            db.delete(InfoContract.TABLE_NAME_EVENTS, null, null);


            // Begin Writing Data
            SQLiteStatement statement = db.compileStatement("INSERT INTO " +
                    InfoContract.TABLE_NAME_EVENTS + "(" +
                    InfoContract.COLUMN_CATEGORY + ", " +
                    InfoContract.COLUMN_TITLE + ", " +
                    InfoContract.COLUMN_PUBDATE + ", " +
                    InfoContract.COLUMN_IMG_RESOURCE + ", " +
                    InfoContract.COLUMN_DESCRIPTION + ") VALUES (?,?,?,?,?);");

            db.beginTransaction();

            for (int j = 0; j < InfoParser.EVENTS.size(); j++) {
                statement.clearBindings();
                for (int i = 0; i < InfoParser.EVENTS.get(j).size(); i++) {
                    statement.bindLong(1, j); // J corresponds to each category
                    statement.bindString(2, InfoParser.EVENTS.get(j).get(i).getTitle());
                    statement.bindString(3, InfoParser.EVENTS.get(j).get(i).getDate());
                    statement.bindLong(4, InfoParser.EVENTS.get(j).get(i).getImgResource());
                    statement.bindString(5, InfoParser.EVENTS.get(j).get(i).getDescription());
                    try {
                        statement.execute();
                    } catch (SQLiteConstraintException e) {
                        return Util.GETLIST_DATABASE_FAILURE;
                    }
                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

        } else { // Otherwise, just read the data

            db = eventStore.getReadableDatabase();

            selection = InfoContract.COLUMN_CATEGORY + "= ?";
            selectionArgs = new String[1];

            for (int j = 0; j < InfoParser.EVENTS.size(); j++) {

                ArrayList<EventItem> eventCategory = new ArrayList<>();
                selectionArgs[0] = "" + j;
                c = db.query(InfoContract.TABLE_NAME_EVENTS, projection, selection, selectionArgs,
                        null, null, null);
                c.moveToFirst();

                for (int i = 0; i < c.getCount(); i++) {
                    eventCategory.add(new EventItem(
                            c.getString(c.getColumnIndexOrThrow(InfoContract.COLUMN_TITLE)),
                            c.getString(c.getColumnIndexOrThrow(InfoContract.COLUMN_PUBDATE)),
                            c.getInt(c.getColumnIndexOrThrow(InfoContract.COLUMN_IMG_RESOURCE)),
                            Util.EVENT_CELL_HEIGHT,
                            c.getString(c.getColumnIndexOrThrow(InfoContract.COLUMN_DESCRIPTION))));
                    c.moveToNext();
                }
                InfoParser.EVENTS.set(j, eventCategory);
                c.close();
            }
            db.close();
            eventStore.close();
        }
        return Util.GETLIST_SUCCESS;
    }
}
