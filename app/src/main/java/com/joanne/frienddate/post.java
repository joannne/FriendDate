package com.joanne.frienddate;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class post extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
    }

    // when user clicks post button
    public void post(View v) {
        // get username from previous activity
        Intent myIntent = getIntent();
        final String username = myIntent.getStringExtra("username");

        // initialize space on view for data
        final EditText place = (EditText) findViewById(R.id.place);
        final EditText date = (EditText) findViewById(R.id.date);
        final EditText time = (EditText) findViewById(R.id.time);
        final EditText activity = (EditText) findViewById(R.id.activity);

        // convert input to string
        final String where = place.getText().toString();
        final String date_day = date.getText().toString();
        final String when = time.getText().toString();
        final String what = activity.getText().toString();

        // check all fields are valid
        if (where.length() == 0)
            place.setError("location of meeting is required!");
        else if (date_day.length() == 0)
            date.setError("Date is required!");
        else if (when.length() == 0)
            time.setError("time is required!");
        else if (what.length() == 0)
            activity.setError("activity is required!");

        // add username to database
        else {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        // run php script to post date
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost("http://addverbum.com/postdate.php");

                        // pass in user input to php script
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                        nameValuePairs.add(new BasicNameValuePair("username", username));
                        nameValuePairs.add(new BasicNameValuePair("date", date_day));
                        nameValuePairs.add(new BasicNameValuePair("where", where));
                        nameValuePairs.add(new BasicNameValuePair("time", when));
                        nameValuePairs.add(new BasicNameValuePair("what", what));

                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        HttpResponse response = httpclient.execute(httppost);
                        response.getEntity();
                    }
                    catch (ClientProtocolException e) {
                    }
                    catch (IOException e) {
                    }
                }
            }).start();

            // direct user to success activity
            Intent postIntent = new Intent(this, post_success.class);
            postIntent.putExtra("username", username);
            startActivity(postIntent);
        }
    }
}

