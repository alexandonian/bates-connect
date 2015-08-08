package com.alexandonian.batesconnect.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

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
        final String SQL_CREATE_INFO_TABLE = "CREATE TABLE " + com.alexandonian.batesconnect.data.InfoContract.TABLE_NAME + " (" +
                InfoContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                InfoContract.COLUMN_INFO + " INTEGER NOT NULL, " +
                InfoContract.COLUMN_MEAL + " INTEGER NOT NULL, " +
                InfoContract.COLUMN_INFOITEM + " TEXT, " +
                InfoContract.COLUMN_TYPE + " INTEGER NOT NULL, " +
                InfoContract.COLUMN_MONTH + " INTEGER NOT NULL, " +
                InfoContract.COLUMN_DAY + " INTEGER NOT NULL, " +
                InfoContract.COLUMN_YEAR + " INTEGER NOT NULL" + ")";

        sqLiteDatabase.execSQL(SQL_CREATE_INFO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + com.alexandonian.batesconnect.data.InfoContract.TABLE_NAME);
        onCreate(db);
    }
}


