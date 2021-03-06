package com.alexandonian.batesconnect.activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.alexandonian.batesconnect.infoItems.Message;
import com.alexandonian.batesconnect.R;
import com.alexandonian.batesconnect.adapters.MingleChatAdapter;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class MingleActivity extends AppCompatActivity {

    public static final String APPLICATION_ID = "GS67hKFFuhA30VRTBjGEbfPIurCUM5bIa8DsMS3p";
    public static final String CLIENT_KEY = "OQXrKoccxLYJBYIUtxuUsqjmHxK84c9YcjVX4cVc";

    public static final String USER_ID_KEY = "userId";

    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    private static final String TAG = MingleActivity.class.getSimpleName();
    private static String mUserId;
    public static ParseUser mUser;

    private EditText etMessage;
    private Button btSend;

    private ListView lvChat;
    private ArrayList<Message> mMessages;
    private MingleChatAdapter mAdapter;
    private int mCurrentMessageCount = -1;

    /**
     * Flag to hold if the activity is running or not.
     */
    private boolean isRunning;

    // Create a handler which can run code periodically
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mingle);

        //User activity_login

        if (ParseUser.getCurrentUser() != null) { // start with existing user
            startWithCurrentUser();
        } else { // If not logged in, activity_login as new anonymous user.
            login();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
        refreshMessages();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning)
                refreshMessages();
        }
    };

    private void refreshMessages() {
        receiveMessage();
        handler.postDelayed(runnable, 500);
    }


    // Get the userId from the cached currentUser object
    private void startWithCurrentUser() {
        mUser = ParseUser.getCurrentUser();
//        mUser.put("online", true);
        mUserId = mUser.getObjectId();
//        mUser.saveEventually();
        setupMessagePosting();
    }

    // Create an anonymous user using ParseAnonymousUtils and set mUserId
    private void login() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Anonymous activity_login failed: " + e.toString());
                } else {
                    startWithCurrentUser();
                }
            }
        });
    }

    // Setup message field and posting
    private void setupMessagePosting() {
        // Get Reference to text field and button
        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
        lvChat = (ListView) findViewById(R.id.lvChat);
        mMessages = new ArrayList<Message>();
        mAdapter = new MingleChatAdapter(MingleActivity.this, mUserId, mMessages);
        lvChat.setAdapter(mAdapter);

        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body = etMessage.getText().toString();
                // Use Message model to create new messages now
                Message message = new Message();
                message.setUserId(mUserId);
                message.setBody(body);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        receiveMessage();
                    }
                });
                etMessage.setText("");
            }
        });
    }

    // Query messages from Parse so we can load them into the chat adapter
    private void receiveMessage() {
        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        // Configure limit and sort order
//        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByAscending("createdAt");
        // Execute the query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {

                    // Only Update the ListView is there are new messages
                    if (mCurrentMessageCount < messages.size() && mMessages != null) {
                        mMessages.clear();
                        mMessages.addAll(messages);
                        mAdapter.notifyDataSetChanged(); // update adapter
                        lvChat.invalidate(); // redraw listview
                        mCurrentMessageCount = mMessages.size();
                    }
                } else {
                    Log.d("message", "Error: " + e.getMessage());
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mingle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
