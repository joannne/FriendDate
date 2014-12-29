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

public class signup extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void validateUser(View v) {

        // get user input and store as string
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        final EditText confirm = (EditText) findViewById(R.id.confirm);

        // validate user input
        if (username.getText().toString().length() == 0) {
            username.setError("username is required!");
        }
        else if (password.getText().toString().length() == 0) {
            password.setError("password is required!");
        }
        else if (confirm.getText().toString().length() == 0) {
            confirm.setError("confirmation password is required!");
        }
        else if (password.getText().toString().equals(confirm.getText().toString()) != true) {
            confirm.setError("passwords must match!");
        }

        // add username to database
        else {
            final String login_name = username.getText().toString();
            final String password_name = password.getText().toString();
            new Thread(new Runnable() {
                public void run() {

                // connect to database via php script on server
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://addverbum.com/addusername.php");

                    // pass in user input to php script
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("username", login_name));
                    nameValuePairs.add(new BasicNameValuePair("password", password_name));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    InputStream myInput = response.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(myInput));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Log.d("Response", line);
                    }

                    // validate username against database
                    if (entity.getContentLength() != 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                username.setError("username already taken");
                                Thread.interrupted();
                            }

                        });
                    }
                    else {
                        // direct user to homepage
                        Intent homepageIntent = new Intent(signup.this, homepage.class);
                        homepageIntent.putExtra("username", username.getText().toString());
                        startActivity(homepageIntent);
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