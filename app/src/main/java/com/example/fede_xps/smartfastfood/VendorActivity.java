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

public class VendorActivity extends AppCompatActivity {

    private String token;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        token= getIntent().getExtras().getString("cookie");
        id= getIntent().getExtras().getString("id");

        Button menu = (Button) findViewById(R.id.vmenu);
        Button order = (Button) findViewById(R.id.vorder);


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open = new Intent(VendorActivity.this, VendorListActivity.class);
                open.putExtra("id", id);
                open.putExtra("cookie", token);

                Log.d("HomeVendor", "open activity");

                startActivity(open);
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VendorActivity.this, OrderActivity.class);
                intent.putExtra("owner_id", id);
                startActivity(intent);
            }
        });
    }
}
