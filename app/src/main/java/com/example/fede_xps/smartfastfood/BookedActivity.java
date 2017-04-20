package com.example.fede_xps.smartfastfood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class BookedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked);

        TextView tv = (TextView) findViewById(R.id.nbooked);

        tv.setText("Your reservation number is: 0014");
    }
}
