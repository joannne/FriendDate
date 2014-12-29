package com.joanne.frienddate;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // if user clicks login button
    public void login (View v) {
        // direct user to login activity
        Intent myIntent = new Intent(this,login.class);
        startActivity(myIntent);
    }

    // if user clicks signup button
    public void signup (View v) {
        // direct user to signup activity
        Intent myIntent = new Intent(this,signup.class);
        startActivity(myIntent);
    }

}
