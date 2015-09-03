package com.alexandonian.batesconnect.demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.alexandonian.batesconnect.R;
import com.alexandonian.batesconnect.demo.custom.CustomActivity;
import com.alexandonian.batesconnect.demo.utils.Utils;
import com.alexandonian.batesconnect.util.Util;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * The Class Login is an Activity class that shows the activity_login screen to users.
 * The current implementation simply includes the options for Login and button
 * for Register. On activity_login button click, it sends the Login details to Parse
 * server to verify user.
 */
public class Login extends CustomActivity {

    /**
     * The username edittext.
     */
    private EditText user;

    /**
     * The password edittext.
     */
    private EditText pwd;

    public static final String PREF_USER_VERIFIED = "user_verified";
    public static final String PREF_USER_EMAIL = "user_email";
    private static boolean mUserVerified;
    private static String mUserEmail;




    /* (non-Javadoc)
         * @see com.chatt.custom.CustomActivity#onCreate(android.os.Bundle)
         */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTouchNClick(R.id.btnLogin);
        setTouchNClick(R.id.btnReg);

        user = (EditText) findViewById(R.id.user);
        pwd = (EditText) findViewById(R.id.pwd);

        // Read in the flag indicating whether or not the user has verified his email address.
        // See PREF_USER_VERIFIED for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mUserVerified = sp.getBoolean(PREF_USER_VERIFIED, false);
        mUserEmail = sp.getString(PREF_USER_EMAIL, "");
        Log.v(Util.LOG_TAG, "" + mUserVerified);
        Log.v(Util.LOG_TAG, mUserEmail);

        if (mUserVerified && !mUserEmail.equals("")) {
            login();
        }
    }

    /* (non-Javadoc)
     * @see com.chatt.custom.CustomActivity#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btnReg) {
            startActivityForResult(new Intent(this, Register.class), 10);
        } else {

            String u = mUserEmail;
            String p = mUserEmail;
            if (u.length() == 0 || p.length() == 0) {
                Utils.showDialog(this, R.string.err_fields_empty);
                return;
            }
            final ProgressDialog dia = ProgressDialog.show(this, null, getString(R.string
                    .alert_wait));
            ParseUser.logInInBackground(u, p, new LogInCallback() {

                @Override
                public void done(ParseUser pu, ParseException e) {
                    dia.dismiss();
                    if (pu != null) {
                        UserList.user = pu;
                        startActivity(new Intent(Login.this, UserList.class));
                        finish();
                    } else {
                        Utils.showDialog(
                                Login.this,
                                getString(R.string.err_login) + " "
                                        + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void login() {
        String u = mUserEmail;
        String p = mUserEmail;
        if (u.length() == 0 || p.length() == 0) {
            Utils.showDialog(this, R.string.err_fields_empty);
            return;
        }
        final ProgressDialog dia = ProgressDialog.show(this, null, getString(R.string
                .alert_login));
        ParseUser.logInInBackground(u, p, new LogInCallback() {

            @Override
            public void done(ParseUser pu, ParseException e) {
                dia.dismiss();
                if (pu != null) {
                    UserList.user = pu;
                    startActivity(new Intent(Login.this, UserList.class));
                    finish();
                } else {
                    Utils.showDialog(
                            Login.this,
                            getString(R.string.err_login) + " "
                                    + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content
     * .Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK)
            finish();

    }

}
