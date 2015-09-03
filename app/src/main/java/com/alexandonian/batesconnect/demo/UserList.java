package com.alexandonian.batesconnect.demo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.alexandonian.batesconnect.R;
import com.alexandonian.batesconnect.demo.custom.CustomActivity;
import com.alexandonian.batesconnect.demo.utils.Const;
import com.alexandonian.batesconnect.demo.utils.Utils;
import com.alexandonian.batesconnect.util.Util;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * The Class UserList is the Activity class. It shows a list of all users of
 * this app. It also shows the Offline/Online status of users.
 */
public class UserList extends CustomActivity {

    /**
     * The Chat list.
     */
    private ArrayList<ParseUser> uList;

    /**
     * The user.
     */
    public static ParseUser user;

    public static boolean inviter;

    // Create a handler which can run code periodically
    private Handler mHandler = new Handler();
    private ProgressDialog progressDialog;
    public static boolean mChatFound = false;
    private static boolean isRunning;

    private Button mingleRefresh;

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        mingleRefresh = (Button) findViewById(R.id.mingle_refresh);
        mingleRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserStatus(true);
                isRunning = true;
                getChat();
            }
        });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        updateUserStatus(true);
        isRunning = true;
        getChat();
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateUserStatus(false);
        isRunning = false;
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateUserStatus(false);
        isRunning = false;
    }

    /**
     * Update user status.
     *
     * @param online true if user is online
     */
    private void updateUserStatus(boolean online) {
        user.put("online", online);
        user.saveEventually();
    }

    private void getChat() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.alert_find_chat));
        progressDialog.setIndeterminate(true);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface
                .OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                isRunning = false;
                dialog.dismiss();
            }
        });
        progressDialog.show();
        findChat();
        mHandler.postDelayed(runnable, 500);
    }

    /**
     * Load list of users.
     */
    private void loadUserList() {
        final ProgressDialog dia = ProgressDialog.show(this, null,
                getString(R.string.alert_loading));
        ParseUser.getQuery().whereNotEqualTo("username", user.getUsername())
                .findInBackground(new FindCallback<ParseUser>() {

                    @Override
                    public void done(List<ParseUser> li, ParseException e) {
                        dia.dismiss();
                        if (li != null) {
                            if (li.size() == 0)
                                Toast.makeText(UserList.this,
                                        R.string.msg_no_user_found,
                                        Toast.LENGTH_SHORT).show();

                            uList = new ArrayList<ParseUser>(li);
                            ListView list = (ListView) findViewById(R.id.list);
                            list.setAdapter(new UserAdapter());
                            list.setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> arg0,
                                                        View arg1, int pos, long arg3) {
                                    startActivity(new Intent(UserList.this,
                                            Chat.class).putExtra(
                                            Const.EXTRA_DATA, uList.get(pos)
                                                    .getUsername()));
                                }
                            });
                        } else {
                            Utils.showDialog(
                                    UserList.this,
                                    getString(R.string.err_users) + " "
                                            + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void findChat() {

        // Check for invites first
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Invite")
                .whereEqualTo("receiver", user.getUsername())
                .whereEqualTo("pending", true);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (list != null && list.size() > 0) {
                    mChatFound = true;
                    progressDialog.dismiss();
                    updateUserStatus(false);
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).put("pending", false);
                        list.get(i).saveEventually();
                    }
                    startActivity(new Intent(UserList.this,
                            Chat.class).putExtra(Const.EXTRA_DATA,
                            list.get(0).getString("sender")));
                }

            }
        });

        if (mChatFound) { // If an invite has been found, don't bother looking for online user
            return;
        } else {

            // Otherwise, look for an online user
            ParseUser.getQuery().whereNotEqualTo("username",
                    user.getUsername()).whereEqualTo("online", true)
                    .findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> li, ParseException e) {
                            if (li != null) {
                                uList = new ArrayList<>(li);
                                if (li.size() > 0) {
                                    mChatFound = true;
                                    inviter = true;
                                    progressDialog.dismiss();
                                    updateUserStatus(false);
                                    startActivity(new Intent(UserList.this,
                                            Chat.class).putExtra(
                                            Const.EXTRA_DATA, uList.get((int) (Math.random() *
                                                    uList.size
                                                            ()))
                                                    .getUsername()));
                                }
                            } else {
                                Utils.showDialog(UserList.this, getString(R.string.err_users) +
                                        " " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!mChatFound && isRunning) {
                findChat();
                mHandler.postDelayed(this, 1000);
                Log.v(Util.LOG_TAG, "Still Searching");
            }
        }
    };


    /**
     * The Class UserAdapter is the adapter class for User ListView. This
     * adapter shows the user name and it's only online status for each item.
     */
    private class UserAdapter extends BaseAdapter {

        /* (non-Javadoc)
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount() {
            return uList.size();
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public ParseUser getItem(int arg0) {
            return uList.get(arg0);
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int pos, View v, ViewGroup arg2) {
            if (v == null)
                v = getLayoutInflater().inflate(R.layout.chat_item, null);

            ParseUser c = getItem(pos);
            TextView lbl = (TextView) v;
            lbl.setText(c.getUsername());
            lbl.setCompoundDrawablesWithIntrinsicBounds(
                    c.getBoolean("online") ? R.drawable.ic_online
                            : R.drawable.ic_offline, 0, R.drawable.arrow, 0);

            return v;
        }

    }
}
