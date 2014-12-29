package com.joanne.frienddate;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class frienddates extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frienddates);

        // get username from previous activity
        Intent myIntent = getIntent();
        final String username = myIntent.getStringExtra("username");

        // set username as global
        global.username = username;

        new Thread(new Runnable() {
                public void run() {
            try {
                // run php script to get json data of frienddates for user
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://addverbum.com/frienddates.php");

                // pass in username to php script
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("username", username));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                response.getEntity();

                // read output from php script
                InputStream myInput = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(myInput));

                // store output in string
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                try {
                    // read json output
                    String result = sb.toString();
                    JSONArray jsonarray = new JSONArray(result);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        String whatfriend = obj.getString("who");

                        // if matches username, get from different column
                        if (whatfriend.equals(username)) {
                            whatfriend = obj.getString("username");
                        }

                        // convert json data to strings
                        final String friend = whatfriend;
                        final String whenday = obj.getString("day");
                        final String whendd = global.parseDate(whenday);
                        final String location = obj.getString("location");
                        final String whent = obj.getString("time");
                        final String timep = whent.substring(0, whent.length() - 3);
                        final String activity = obj.getString("what");

                        runOnUiThread(new Runnable() {
                                                            @Override
                            public void run() {
                            TableLayout stk = (TableLayout) findViewById(R.id.table);
                            TableRow row = new TableRow(frienddates.this);
                            TextView place = new TextView(frienddates.this);
                            TextView who = new TextView(frienddates.this);
                            TextView date = new TextView(frienddates.this);
                            TextView time = new TextView(frienddates.this);
                            TextView what = new TextView(frienddates.this);

                            // pass in string to setText for view
                            place.setText(location);
                            who.setText(friend);
                            time.setText(timep);
                            date.setText(whendd);
                            what.setText(activity);

                            // set padding
                            who.setPadding(5, 0, 0, 0);
                            place.setPadding(20, 0, 0, 0);
                            time.setPadding(30, 0, 0, 0);
                            date.setPadding(30, 0, 0, 0);
                            what.setPadding(30, 0, 0, 0);

                            // add textViews to row
                            row.addView(who);
                            row.addView(place);
                            row.addView(date);
                            row.addView(time);
                            row.addView(what);

                            // add row to table
                            stk.addView(row);
                                                            }

                        });
                    }
                }

                catch (JSONException e) {
                }
            }

            catch (ClientProtocolException e) {
            }
            catch (IOException e) {
            }
            }
            }).start();

        // if user clicks button
        Button clickButton = (Button) findViewById(R.id.button);
        clickButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            // direct user to homepage
            Intent homepageIntent = new Intent(frienddates.this, homepage.class);
            homepageIntent.putExtra("username", username);
            startActivity(homepageIntent);
            }
        });
    }
}