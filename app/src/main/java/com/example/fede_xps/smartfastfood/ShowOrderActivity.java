package com.example.fede_xps.smartfastfood;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class ShowOrderActivity extends AppCompatActivity{

    private ArrayList<String> listdata;
    private ArrayAdapter adapter;
    private ListView listView;
    private String token;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);

        token = getIntent().getExtras().getString("token");
        code = getIntent().getExtras().getString("code");


        OrderItemTask or = new OrderItemTask(code, token);
        or.execute( (Void) null);

    }





    public class OrderItemTask extends AsyncTask<Void, Void, String> {

        private final String mToken;
        private final String mCode;

        public OrderItemTask(String code, String token) {

            mToken=token;
            mCode=code;

        }

        @Override
        protected String doInBackground(Void... params) {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://smartfastfood-nikkolo94.c9users.io/getOrderList");

            String TAG ="ORDER ITEM";



            try {
                httpGet.addHeader("token1", mToken);
                httpGet.addHeader("code", mCode);

                Log.d(TAG, "Start connection");

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

            listdata = new ArrayList<String>();
            if (jArray != null) {
                for (int i=0;i<jArray.length();i++){
                    String j = jArray.getJSONObject(i).getString("name");
                    Log.d("ITEM4", j);
                    listdata.add(j);
                }
            }
            Log.d("My App", jArray.toString());

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + jArray + "\"");
        }


        listView = (ListView) findViewById(R.id.listView4);
        adapter = new ArrayAdapter<String>(this, R.layout.item4, listdata);
        listView.setAdapter(adapter);

    }
}
