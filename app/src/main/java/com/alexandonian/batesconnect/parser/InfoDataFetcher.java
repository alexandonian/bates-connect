package com.alexandonian.batesconnect.parser;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alexandonian.batesconnect.data.InfoDbHelper;
import com.alexandonian.batesconnect.util.Util;

/**
 * Created by Administrator on 7/21/2015.
 */
public class InfoDataFetcher {

    public static int fetchData(Context context, int month, int day, int year){

        InfoDbHelper infoStore = new InfoDbHelper(context);
        SQLiteDatabase db;

        if (infoStore == null){
            return Util.GETLIST_DATABASE_FAILURE;
        }

        db = infoStore.getReadableDatabase();

        String selection = InfoDbHelper.InfoEntry.COLUMN_MONTH + "= ? AND " + InfoDbHelper.InfoEntry
                .COLUMN_DAY + "= ? AND " + InfoDbHelper.InfoEntry.COLUMN_YEAR + "= ?";
        String[] selectionArgs = new String[3];

        selectionArgs[0] = "" + month;
        selectionArgs[1] = "" + day;
        selectionArgs[2] = "" + year;

        String[] projection = {
                InfoDbHelper.InfoEntry.COLUMN_MONTH,
                InfoDbHelper.InfoEntry.COLUMN_DAY,
                InfoDbHelper.InfoEntry.COLUMN_YEAR
        };

        Cursor cursor = db.query(InfoDbHelper.InfoEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);
        cursor.moveToFirst();

        boolean cursorExists = (cursor.getCount() == 0);
        cursor.close();
        db.close();

        // If data for today does not exist, or a manual refresh is requested,
        // download/store data
        if (cursorExists || InfoParser.manualRefresh){
            int result = InfoParser.getInfoList(month, day, year);
            if (result != Util.GETLIST_SUCCESS){
                return result;
            }
        }


    }
}
