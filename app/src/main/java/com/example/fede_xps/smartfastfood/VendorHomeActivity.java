package com.example.fede_xps.smartfastfood;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;

public class VendorHomeActivity extends AppCompatActivity {
    ArrayList<Item> listdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String token= getIntent().getExtras().getString("cookie");
        String id= getIntent().getExtras().getString("id");
        ListRequestTask lr =new ListRequestTask(token, id);
        lr.execute( (Void) null);

    }

    public class ListRequestTask extends AsyncTask<Void, Void, String> {

        private final String mCookie;
        private final String mUrl;

        ListRequestTask(String cookie, String url) {
            mUrl = url;
            mCookie = cookie;
        }

        @Override
        protected String doInBackground(Void... params) {



            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://smartfastfood-nikkolo94.c9users.io/list/"+mUrl);

            String TAG ="MiaLista:";

            try {
                Log.d(TAG, mCookie);
                httpGet.addHeader("cookie1", mCookie);


                HttpResponse response = httpClient.execute(httpGet);
                //int statusCode = response.getStatusLine().getStatusCode();
                final String responseBody = EntityUtils.toString(response.getEntity());
                Log.d(TAG, "Response: " + responseBody);

                return responseBody;


            } catch (ClientProtocolException e) {
                Log.e(TAG, "Error sending ID token to backend.", e);
            } catch (IOException e) {
                Log.e(TAG, "Error sending ID token to backend.", e);
            }

            return "default";
        }

        @Override
        protected void onPostExecute(String s) {
            if(!s.equals("default"))
                creaLista(s);

        }

        @Override
        protected void onCancelled() {

        }
    }

    private void creaLista(String s) {
        JSONArray jArray = null;
        listdata = null;

        try {
            jArray = new JSONArray(s);

            listdata = new ArrayList<Item>();
            if (jArray != null) {
                for (int i=0;i<jArray.length();i++){
                    Item j = new Item(jArray.getJSONObject(i));
                    Log.d("ITEM", j.getJson().toString());
                    listdata.add(j);
                }
            }
            Log.d("My App", jArray.toString());

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + jArray + "\"");
        }

        ListView listView = (ListView) findViewById(R.id.listView2);


        CustomListViewAdapter adapter = new CustomListViewAdapter(this, R.layout.item, listdata);



        listView.setAdapter(adapter);

        Button cart = (Button) findViewById(R.id.delete);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> al = new ArrayList<String>();

                for(Item i: listdata) {
                    if(i.getCheck()) {
                        String s = i.getJson().toString();
                        //Log.d("onClick", s);
                        al.add(s);
                    }
                }

                if(al.isEmpty()) {
                    Toast.makeText(VendorHomeActivity.this,
                            "Seleziona almeno un argomento!" , Toast.LENGTH_LONG).show();

                    return;
                }


            }
        });
    }
}
