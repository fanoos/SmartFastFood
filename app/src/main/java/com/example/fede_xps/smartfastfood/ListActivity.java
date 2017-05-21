package com.example.fede_xps.smartfastfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {


    private ArrayAdapter<JSONObject> listAdapter;
    ArrayList<Item> listdata;
    CustomListViewAdapter adapter;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        String s= getIntent().getExtras().getString("json");
        token= getIntent().getExtras().getString("token");

        Log.d("LIST", "stringa :"+s);
        Log.d("LIST", token);

        JSONArray jArray = null;
        listdata = null;

        try {
            jArray = new JSONArray(s);

            listdata = new ArrayList<Item>();
            if (jArray != null) {
                for (int i=0;i<jArray.length();i++){
                    Item j = new Item(jArray.getJSONObject(i));
                    listdata.add(j);
                }
            }
            Log.d("My App", jArray.toString());

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + jArray + "\"");
        }

        ListView listView = (ListView) findViewById(R.id.listView1);


        adapter = new CustomListViewAdapter(this, R.layout.item, listdata);



        listView.setAdapter(adapter);

        Button cart = (Button) findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> al = new ArrayList<String>();

                for(Item i: listdata) {
                    if(i.getCheck()) {
                        try {
                            String s = i.getJson().toString();
                            //Log.d("onClick", s);
                            al.add(s);
                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON");
                        }
                    }
                }

                if(al.isEmpty()) {
                    Toast.makeText(ListActivity.this,
                            "Seleziona almeno un argomento!" , Toast.LENGTH_LONG).show();

                    return;
                }

                Intent start = new Intent(ListActivity.this, CartActivity.class);

                Bundle extra = new Bundle();
                extra.putSerializable("list", al);
                start.putExtra("extra", extra);
                start.putExtra("token", token);

                startActivity(start);
            }
        });

    }

}
