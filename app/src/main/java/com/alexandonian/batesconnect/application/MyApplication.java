package com.alexandonian.batesconnect.application;

import android.app.Application;

import com.alexandonian.batesconnect.infoItems.Message;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Administrator on 8/25/2015.
 */
public class MyApplication extends Application {

    public static final String APPLICATION_ID = "GS67hKFFuhA30VRTBjGEbfPIurCUM5bIa8DsMS3p";
    public static final String CLIENT_KEY = "OQXrKoccxLYJBYIUtxuUsqjmHxK84c9YcjVX4cVc";

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // activity_register your parse models here
//        ParseObject.registerSubclass(Message.class);

        // Existing initialization happens after all classes are registered
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        ParseUser.enableRevocableSessionInBackground();
        ParseInstallation.getCurrentInstallation().saveInBackground();
//        ParseUser.enableAutomaticUser();
//        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
//        ParseACL.setDefaultACL(defaultACL, true);
//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();
    }
}
