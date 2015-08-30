package com.alexandonian.batesconnect.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 7/21/2015.
 */
public class InfoDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "info.db";

    public InfoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MENU_TABLE = "CREATE TABLE " + InfoContract.TABLE_NAME + " (" +
                InfoContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                InfoContract.COLUMN_INFO + " INTEGER NOT NULL, " +
                InfoContract.COLUMN_MEAL + " INTEGER NOT NULL, " +
                InfoContract.COLUMN_MENUITEM + " TEXT, " +
                InfoContract.COLUMN_TYPE + " INTEGER NOT NULL, " +
                InfoContract.COLUMN_MONTH + " INTEGER NOT NULL, " +
                InfoContract.COLUMN_DAY + " INTEGER NOT NULL, " +
                InfoContract.COLUMN_YEAR + " INTEGER NOT NULL" + ")";

        final String SQL_CREATE_EVENTS_TABLE = "CREATE TABLE " +
                InfoContract.TABLE_NAME_EVENTS + " (" +
                InfoContract.COLUMN_ID_EVENTS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                InfoContract.COLUMN_CATEGORY + " INTEGER NOT NULL, " +
                InfoContract.COLUMN_TITLE + " TEXT, " +
                InfoContract.COLUMN_PUBDATE + " TEXT, " +
                InfoContract.COLUMN_IMG_RESOURCE + " INTEGER NOT NULL, " +
                InfoContract.COLUMN_DESCRIPTION + " TEXT" + ")";

        sqLiteDatabase.execSQL(SQL_CREATE_MENU_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_EVENTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + InfoContract.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + InfoContract.TABLE_NAME_EVENTS);
        onCreate(db);
    }
}


