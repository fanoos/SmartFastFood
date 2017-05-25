package com.example.fede_xps.smartfastfood;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class AddActivity extends AppCompatActivity {

    String token;
    String id;
    String name;
    String price;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        token= getIntent().getExtras().getString("cookie");
        id= getIntent().getExtras().getString("id");


        Button add = (Button) findViewById(R.id.add);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText name1= (EditText) findViewById(R.id.name);
                name = name1.getText().toString();
                EditText price1= (EditText) findViewById(R.id.price);
                price = price1.getText().toString();
                EditText image1= (EditText) findViewById(R.id.image1);
                image = image1.getText().toString();

                ListRequestTask lr = new ListRequestTask(token, id, name, price, image);
                lr.execute((Void) null);
            }
        });



    }


    public class ListRequestTask extends AsyncTask<Void, Void, String> {

        private final String mCookie;
        private final String mUrl;
        private final String mName;
        private final String mPrice;
        private final String mImage;


        ListRequestTask(String cookie, String url, String name, String price, String image) {
            mUrl = url;
            mCookie = cookie;
            mName = name;
            mPrice = price;
            mImage = image;
        }

        @Override
        protected String doInBackground(Void... params) {

            Log.d("mecellanegro", mName);
            Log.d("mecellanegro", price);
            Log.d("mecellanegro", mImage);

            HttpClient httpClient = new DefaultHttpClient();
            //HttpGet httpGet = new HttpGet("https://smartfastfood-nikkolo94.c9users.io/list/"+mUrl);
            HttpPost httpPost = new HttpPost("https://smartfastfood-nikkolo94.c9users.io/add");

            String TAG ="MiaLista:";

            try {
                Log.d(TAG, mCookie);
                httpPost.addHeader("cookie1", mCookie);
                httpPost.addHeader("name1", mName);
                httpPost.addHeader("price1", mPrice);
                httpPost.addHeader("image1", mImage);
                httpPost.addHeader("id", mUrl);




                HttpResponse response = httpClient.execute(httpPost);
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
            if(s.equals("ok")){
                Intent open = new Intent(AddActivity.this, VendorListActivity.class);
                open.putExtra("id", id);
                open.putExtra("cookie", token);

                Log.d("HomeVendor", "open activity");

                startActivity(open);
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
}
