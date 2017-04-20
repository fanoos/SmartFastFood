package com.example.fede_xps.smartfastfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {


    int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Bundle extra = getIntent().getBundleExtra("extra");
        ArrayList<String> list = (ArrayList<String>) extra.getSerializable("list");

        //Log.d("qui",list.get(0));

        ArrayList<Item> arr = new ArrayList<Item>();

        for(String s: list) {
            try {
                Item i = new Item(new JSONObject(s));
                arr.add(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        total = 0;

        for(Item i: arr) {
            try {
                total += i.getJson().getInt("price");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        TextView et = (TextView) findViewById(R.id.tot);

        et.setText("Tot: €"+total);

        ListView listView = (ListView) findViewById(R.id.listView2);

        CustomListViewAdapter adapter = new CustomListViewAdapter(this, R.layout.item2, arr
        );


        listView.setAdapter(adapter);

        Button pay = (Button) findViewById(R.id.payC);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(CartActivity.this, BookedActivity.class);
                startActivity(start);
            }
        });

        Button pay1 = (Button) findViewById(R.id.payP);
        pay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(CartActivity.this, BookedActivity.class);
                startActivity(start);
            }
        });


    }
}
