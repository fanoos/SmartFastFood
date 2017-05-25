package com.example.fede_xps.smartfastfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;

public class HomeEmployActivity extends AppCompatActivity {

    private String token;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_employ);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        token= getIntent().getExtras().getString("cookie");
        id= getIntent().getExtras().getString("id");


        Button order= (Button) findViewById(R.id.vorder1);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeEmployActivity.this, OrderActivity.class);
                intent.putExtra("owner_id", id);
                startActivity(intent);
            }
        });


    }
}
