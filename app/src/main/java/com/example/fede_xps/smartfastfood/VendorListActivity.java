package com.example.fede_xps.smartfastfood;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class VendorListActivity extends AppCompatActivity {
    ArrayList<Item> listdata;
    String token;
    String id;
    boolean check;
    CustomListViewAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        check=false;




        token= getIntent().getExtras().getString("cookie");
        id= getIntent().getExtras().getString("id");

        Button add = (Button) findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAdd();
            }
        });


        ListRequestTask lr =new ListRequestTask(token, id);
        lr.execute( (Void) null);

    }

    private void goAdd() {

        Intent intent = new Intent(VendorListActivity.this, AddActivity.class);

        intent.putExtra("cookie", token);
        intent.putExtra("id", id);


        startActivity(intent);
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
        adapter = null;

        try {
            jArray = new JSONArray(s);

            listdata = new ArrayList<Item>();
            if (jArray != null) {
                for (int i=0;i<jArray.length();i++){
                    Item j = new Item(jArray.getJSONObject(i));
                    Log.d("ITEM", j.getJson().toString()+"\n");
                    listdata.add(j);
                }
            }
            Log.d("My App", jArray.toString());

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + jArray + "\"");
        }

        listView = (ListView) findViewById(R.id.listView2);


        adapter = new CustomListViewAdapter(this, R.layout.item, listdata);



        listView.setAdapter(adapter);

        Button delete = (Button) findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(check)
                    return;

                check = true;

                ArrayList<String> al = new ArrayList<String>();

                for(Item i: listdata) {
                    if(i.getCheck()) {
                        String s = i.getJson().toString();
                        Log.d("onClick", s);
                        al.add(s);
                    }
                }

                if(al.isEmpty()) {
                    Toast.makeText(VendorListActivity.this,
                            "Seleziona almeno un argomento!" , Toast.LENGTH_LONG).show();

                    return;
                }

                DeleteItemTask dl = new DeleteItemTask(id, token, al);
                dl.execute( (Void) null);
            }
        });
    }

    public class DeleteItemTask extends AsyncTask<Void, Void, String> {

        private final String mToken;
        private final String mId;
        private final ArrayList<String> ala;

        public DeleteItemTask(String id, String token, ArrayList<String> al) {

            mToken=token;
            mId=id;
            ala = al;

        }

        @Override
        protected String doInBackground(Void... params) {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://smartfastfood-nikkolo94.c9users.io/delete/"+mId);

            String TAG ="DELETE ITEM";

            JSONArray ja = new JSONArray();

            for(String i : ala) {
                ja.put(i);
            }

            try {
                httpGet.addHeader("cookie1", mToken);
                httpGet.addHeader("list", ja.toString());

                Log.d(TAG, "Start connection");
                Log.d(TAG, ja.toString());

                HttpResponse response = httpClient.execute(httpGet);

                int statusCode= response.getStatusLine().getStatusCode();
                if( statusCode != 200 ) {
                    return "Errore server!";
                }

                final String responseBody = EntityUtils.toString(response.getEntity());
                Log.d(TAG, "response: " + responseBody);

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

            if(s.equals("no cookie") || s.equals("no id")) {
                Log.d("VENDOR",s);

            } else {

                creaLista2(s);
            }

            check = false;


        }

        @Override
        protected void onCancelled() {

            check = false;


        }
    }

    private void creaLista2(String s) {

        JSONArray jArray = null;
        listdata = null;

        try {
            jArray = new JSONArray(s);

            listdata = new ArrayList<Item>();
            if (jArray != null) {
                for (int i=0;i<jArray.length();i++){
                    Item j = new Item(jArray.getJSONObject(i));
                    Log.d("ITEM", j.getJson().toString()+"\n");
                    listdata.add(j);
                }
            }
            Log.d("My App", jArray.toString());

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + jArray + "\"");
        }


        adapter = null;
        adapter = new CustomListViewAdapter(this, R.layout.item, listdata);
        listView.setAdapter(adapter);


    }



}
