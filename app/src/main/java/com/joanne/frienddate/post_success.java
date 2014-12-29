package com.joanne.frienddate;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

public class post_success extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_success);
    }

    // if user clicks return to homepage button
    public void returnhome(View v) {
        // get username from previous activity
        Intent myIntent = getIntent();
        final String username = myIntent.getStringExtra("username");

        // direct user back to homepage
        Intent returnIntent = new Intent(this, homepage.class);
        returnIntent.putExtra("username", username);
        startActivity(returnIntent);
    }
}
