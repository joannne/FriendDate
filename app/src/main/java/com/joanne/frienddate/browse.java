package com.joanne.frienddate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;


public class browse extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        // get username from previous activity
        Intent myIntent = getIntent();
        final String username = myIntent.getStringExtra("username");

        new Thread(new Runnable() {
            public void run() {
                try {
                    // connect to database via php script
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://addverbum.com/browse.php");

                    // pass in user input to php script
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("username", username));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    response.getEntity();

                    // initialize inputstream, reader, and line to read user input
                    InputStream myInput = response.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(myInput));
                    String line;
                    StringBuilder sb = new StringBuilder();

                    // read user input into string
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    try {
                        // get string containing data
                        String result = sb.toString();

                        // parse json data
                        JSONArray jsonarray = new JSONArray(result);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject obj = jsonarray.getJSONObject(i);
                            final String user = obj.getString("username");
                            final String whenday = obj.getString("day");
                            final String location = obj.getString("location");
                            final String whentime = obj.getString("time");
                            final String timep = whentime.substring(0, whentime.length() - 3);
                            final String activity = obj.getString("what");
                            final String whenddate = global.parseDate(whenday);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                // initialize data for rows in table
                                TableLayout stk = (TableLayout) findViewById(R.id.main);
                                TableRow row = new TableRow(browse.this);
                                row.setClickable(true);
                                row.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick (View v) {
                                        alertconfirm(user, whenday, location, whentime, activity);
                                    }
                                });

                                // initialize textviews
                                TextView place = new TextView(browse.this);
                                TextView who = new TextView(browse.this);
                                TextView date = new TextView(browse.this);
                                TextView time = new TextView(browse.this);
                                TextView what = new TextView(browse.this);

                                // pass in json data and set as text
                                place.setText(location);
                                who.setText(user);
                                time.setText(timep);
                                date.setText(whenddate);
                                what.setText(activity);

                                // set padding for rows
                                who.setPadding(5, 0, 0, 0);
                                place.setPadding(30, 0, 0, 0);
                                time.setPadding(30, 0, 0, 0);
                                date.setPadding(30, 0, 0, 0);
                                what.setPadding(30, 0, 0, 0);

                                // add textview as row
                                row.addView(who);
                                row.addView(place);
                                row.addView(date);
                                row.addView(time);
                                row.addView(what);
                                stk.addView(row);
                                }

                            });
                        }
                    }
                    catch (JSONException e) {
                        Log.d("Error", "JSON error");
                    }
                }
                catch (ClientProtocolException e) {
                }
                catch (IOException e) {
                }
            }
        }).start();

        // check if user clicks back button
        Button clickButton = (Button) findViewById(R.id.button);
        clickButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // direct user back to homepage
                Intent homepageIntent = new Intent(browse.this, homepage.class);
                homepageIntent.putExtra("username", username);
                startActivity(homepageIntent);
            }
        });
    }

    // if user clicks to claim frienddate
    public void alertconfirm(String friend, String whend, String location, String whentime, String activity) {
        // get username from previous activity
        Intent myIntent = getIntent();
        final String username = myIntent.getStringExtra("username");
        final String user = friend;
        final String day = whend;
        final String where = location;
        final String time = whentime;
        final String what = activity;

        // initialize confirmation (via alert dialog)
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(browse.this);
        alertDialog2.setTitle("Confirm?");
        alertDialog2.setMessage("Do you want to claim this frienddate?");

        // if user clicks yes to confirmation
        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "You have claimed this frienddate!", Toast.LENGTH_SHORT).show();
                        // delete request from browse
                        new Thread(new Runnable() {
                            public void run() {
                            try {
                                // run php script to delete post from database
                                HttpClient httpclient = new DefaultHttpClient();
                                HttpPost httppost = new HttpPost("http://addverbum.com/remove_post.php");
                                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
                                nameValuePairs.add(new BasicNameValuePair("username", user));
                                nameValuePairs.add(new BasicNameValuePair("date", day));
                                nameValuePairs.add(new BasicNameValuePair("where", where));
                                nameValuePairs.add(new BasicNameValuePair("time", time));
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

                        // add claimed date to user's log of frienddates
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    // run php script to add claimed date
                                    HttpClient httpclient = new DefaultHttpClient();
                                    HttpPost httppost = new HttpPost("http://addverbum.com/adddate.php");
                                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
                                    nameValuePairs.add(new BasicNameValuePair("username", username));
                                    nameValuePairs.add(new BasicNameValuePair("date", day));
                                    nameValuePairs.add(new BasicNameValuePair("who", user));
                                    nameValuePairs.add(new BasicNameValuePair("where", where));
                                    nameValuePairs.add(new BasicNameValuePair("time", time));
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

                        // refresh activity page
                        finish();
                        startActivity(getIntent());
                    }
                });

        // if user clicks no to confirmation
        alertDialog2.setNegativeButton("NO",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Never mind! ", Toast.LENGTH_SHORT).show();
                dialog.cancel();
                }
            });

        // display alert dialog
        alertDialog2.show();
    }
}