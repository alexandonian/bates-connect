package com.alexandonian.batesconnect.demo;

import android.app.Application;

import com.parse.Parse;

/**
 * The Class ChattApp is the Main Application class of this app. The onCreate
 * method of this class initializes the Parse.
 */
public class ChattApp extends Application {

    /* (non-Javadoc)
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "GS67hKFFuhA30VRTBjGEbfPIurCUM5bIa8DsMS3p",
                "OQXrKoccxLYJBYIUtxuUsqjmHxK84c9YcjVX4cVc");
    }
}
