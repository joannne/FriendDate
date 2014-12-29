package com.joanne.frienddate;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class login extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    // when user clicks "log in" button, validateUser is called
    public void validateUser(View v) {

        // get user input and store as string
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        final String login_name = username.getText().toString();
        final String password_name = password.getText().toString();

        // validate user input
        if (login_name.length() == 0)
            username.setError("username required!");
        else if (password_name.length() == 0)
            password.setError("password required!");
        else {
            new Thread(new Runnable() {
                public void run() {

                // connect to mysql database via php script on server
                try {
                    HttpClient httpclient = new DefaultHttpClient();

                    // execute php script to access database
                    HttpPost httppost = new HttpPost("http://addverbum.com/validatelogin.php");

                    // pass in user input to php script
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("username", login_name));
                    nameValuePairs.add(new BasicNameValuePair("password", password_name));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    // establish input stream and reader for php script's response
                    InputStream myInput = response.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(myInput));

                    // if database query did not validate user input
                    if (entity.getContentLength() == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                password.setError("wrong password or user was not found");
                                Thread.interrupted();
                            }

                        });
                    }
                    else {
                        // direct user to homepage
                        Intent myIntent = new Intent(login.this, homepage.class);
                        myIntent.putExtra("username", login_name);
                        startActivity(myIntent);
                    }

                }
                catch (ClientProtocolException e) {
                }
                catch (IOException e) {
                }
                }
            }).start();
        }
    }
}