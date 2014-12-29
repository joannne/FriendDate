package com.joanne.frienddate;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class homepage extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // retrieve username
        Intent myIntent = getIntent();
        final String username = myIntent.getStringExtra("username");

        // dynamically generate welcome textview to welcome user
        TextView welcome = (TextView) findViewById(R.id.welcome);
        welcome.setText("Welcome, " + username);

        final Button add = (Button) findViewById(R.id.button);

        // if user clicks add button, direct to add
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            Intent addIntent = new Intent(homepage.this, add.class);
            addIntent.putExtra("username", username);
            startActivity(addIntent);
            }
        });

        // if user clicks browse button, direct to browse
        Button browse = (Button) findViewById(R.id.browse);
        browse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browseIntent = new Intent(homepage.this, browse.class);
                browseIntent.putExtra("username", username);
                startActivity(browseIntent);
            }
        });

        // if user clicks post button, direct to post
        Button post = (Button) findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent postIntent = new Intent(homepage.this, post.class);
                postIntent.putExtra("username", username);
                startActivity(postIntent);
            }
        });

        // if user clicks frienddates button, direct to frienddates
        Button frienddates = (Button) findViewById(R.id.frienddates);
        frienddates.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent friendIntent = new Intent(homepage.this, frienddates.class);
                friendIntent.putExtra("username", username);
                startActivity(friendIntent);
            }
        });
    }
}
