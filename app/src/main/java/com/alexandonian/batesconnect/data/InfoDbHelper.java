package com.alexandonian.batesconnect.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Administrator on 7/21/2015.
 */
public class InfoDbHelper extends SQLiteOpenHelper{

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "info.db";

    public InfoDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String  SQL_CREATE_INFO_TABLE = "CREATE TABLE" + InfoEntry.TABLE_NAME + " (" +
                InfoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                InfoEntry.COLUMN_INFO + " INTEGER NOT NULL, " +
                InfoEntry.COLUMN_MEAL + " INTEGER NOT NULL, " +
                InfoEntry.COLUMN_INFOITEM + " TEXT, " +
                InfoEntry.COLUMN_MONTH + " INTEGER NOT NULL, " +
                InfoEntry.COLUMN_DAY + " INTEGER NOT NULL, " +
                InfoEntry.COLUMN_YEAR + " INTEGER NOT NULL, " + ")";

        sqLiteDatabase.execSQL(SQL_CREATE_INFO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + InfoEntry.TABLE_NAME);
        onCreate(db);
    }

    public static final class InfoEntry implements BaseColumns {

        public static final String TABLE_NAME = "meal";

        public static final String COLUMN_ID = "_id",
                COLUMN_INFOITEM = "infoitem",
                COLUMN_MEAL = "meal",
                COLUMN_INFO = "info",
                COLUMN_MONTH = "month",
                COLUMN_DAY = "day",
                COLUMN_YEAR = "year";


}
}


