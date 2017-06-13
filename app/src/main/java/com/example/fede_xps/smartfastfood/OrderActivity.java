package com.example.fede_xps.smartfastfood;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Handler;

import java.io.IOException;
import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    private ListView listView;
    private CustomListViewAdapterOrder adapter;
    ArrayList<Item3> listdata;
    String owner_id;
    private Handler h;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        owner_id = getIntent().getExtras().getString("owner_id");

        h = new Handler();
        h.postDelayed(myRunnable2, 1000);

    }

    private Runnable myRunnable2 = new Runnable() {
        public void run() {
            // do some thing
            ListRequestTask1 lr = new ListRequestTask1(owner_id);
            lr.execute( (Void) null);
            h.postDelayed(myRunnable2, 5000);

        }
    };

    public void creaLista(String s) {


        JSONArray jArray = null;
        listdata = null;
        adapter = null;

        try {
            jArray = new JSONArray(s);

            listdata = new ArrayList<Item3>();
            if (jArray != null) {
                for (int i=0;i<jArray.length();i++){
                    Item3 j = new Item3(jArray.getJSONObject(i).getString("id_order"), jArray.getJSONObject(i).getString("pagato"));
                    Log.d("ITEM3", j.getCode()+" "+j.getPay()+"\n");
                    listdata.add(j);
                }
            }
            Log.d("My App", jArray.toString());

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + jArray + "\"");
        }


        listView = (ListView) findViewById(R.id.listView3);
        adapter = new CustomListViewAdapterOrder(this, R.layout.item3, listdata);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                TextView tv = (TextView) arg1.findViewById(R.id.number);
                String code = tv.getText().toString();

                Intent intent = new Intent(OrderActivity.this, ShowOrderActivity.class);
                intent.putExtra("code", code);
                intent.putExtra("token", owner_id);
                startActivity(intent);

            }
        });
    }

    public class ListRequestTask1 extends AsyncTask<Void, Void, String> {

        private final String mId;

        ListRequestTask1(String id) {
            mId = id;
        }

        @Override
        protected String doInBackground(Void... params) {



            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://smartfastfood-nikkolo94.c9users.io/orderGet");

            String TAG ="OrderList";

            try {
                Log.d(TAG, mId);
                httpGet.addHeader("owner_id", mId);


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
}
