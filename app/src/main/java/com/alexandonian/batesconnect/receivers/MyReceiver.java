package com.alexandonian.batesconnect.receivers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alexandonian.batesconnect.demo.Chat;
import com.alexandonian.batesconnect.demo.utils.Const;
import com.alexandonian.batesconnect.util.Util;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

public class MyReceiver extends ParsePushBroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            Log.v(Util.LOG_TAG, json.getString("sendUser"));
            if (json.has("sendUser")) {
                Intent i = new Intent(context, Chat.class);
                i.putExtra(Const.EXTRA_DATA, json.getString("sendUser"));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } else{
                super.onPushOpen(context, intent);
            }
        } catch (JSONException e) {
            Log.d("notification", "JSON exception: " + e.getMessage());
            e.printStackTrace();
        }


    }

}
