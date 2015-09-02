package com.alexandonian.batesconnect.demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

import com.alexandonian.batesconnect.R;
import com.alexandonian.batesconnect.demo.custom.CustomActivity;
import com.alexandonian.batesconnect.demo.utils.Utils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

/**
 * The Class Register is the Activity class that shows user registration screen
 * that allows user to register itself on Parse server for this Chat app.
 */
public class Register extends CustomActivity {

    /**
     * The username EditText.
     */
    private EditText user;

    /**
     * The password EditText.
     */
    private EditText pwd;

    /**
     * The email EditText.
     */
    private EditText email;

    /* (non-Javadoc)
     * @see com.chatt.custom.CustomActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        setTouchNClick(R.id.btnReg);

        user = (EditText) findViewById(R.id.user);
        pwd = (EditText) findViewById(R.id.pwd);
        email = (EditText) findViewById(R.id.email);
    }

    /* (non-Javadoc)
     * @see com.chatt.custom.CustomActivity#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        super.onClick(v);

        String u = user.getText().toString();
        String p = pwd.getText().toString();
        String e = email.getText().toString();
        if (e.length() == 0) {
            Utils.showDialog(this, R.string.err_fields_empty);
            return;
        }
        final ProgressDialog dia = ProgressDialog.show(this, null,
                getString(R.string.alert_wait));

        final ParseUser pu = new ParseUser();
        pu.setEmail(e);
        pu.setPassword(e);
        pu.setUsername(e);
        pu.signUpInBackground(new SignUpCallback() {

            @Override
            public void done(ParseException e) {
                dia.dismiss();
                if (e == null) {
                    UserList.user = pu;
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences
                            (getApplicationContext());
                    sp.edit().putBoolean(Login.PREF_USER_VERIFIED, true).apply();
                    sp.edit().putString(Login.PREF_USER_EMAIL, pu.getUsername()).apply();
                    startActivity(new Intent(Register.this, UserList.class));
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Utils.showDialog(
                            Register.this,
                            getString(R.string.err_singup) + " "
                                    + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("username", e);
        installation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Utils.showDialog(Register.this, getString(R.string.err_singup) + "" + e
                            .getMessage());
                }
            }
        });

    }
}
