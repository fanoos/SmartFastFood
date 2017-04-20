package com.example.fede_xps.smartfastfood;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QRCodeActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private QRCodeReaderView qrCodeReaderView;
    //private TextView resultTextView;

    private boolean just;
    private String cookieUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        just = false;

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 50);
        }
        setContentView(R.layout.activity_qrcode);

        //resultTextView=(TextView) findViewById(R.id.info1);
        cookieUser = getIntent().getExtras().getString("cookie");


        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
        qrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        if(just) {
            return;
        }
        Log.d("JUST", just+"");
        just=true;

        Log.d("camera", text);

        qrCodeReaderView.stopCamera();

        //chiamata al backend per ricevere la lista del menu
        ListRequestTask lr = new ListRequestTask(text, cookieUser);
        lr.execute((Void)null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        just=false;
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }

    protected void creaLista(String s) {

        //TODO INSERIRE QUI CODICE PER CREARE NUOVA INTENT A LISTA MENU

        if(s.equals("errore")) {
            qrCodeReaderView.startCamera();
            return;
        }

        Intent start = new Intent(QRCodeActivity.this, ListActivity.class);
        start.putExtra("json", s);
        //just=false;
        startActivity(start);
    }




    public class ListRequestTask extends AsyncTask<Void, Void, String> {

        private final String mCookie;
        private final String mUrl;

        ListRequestTask(String url, String cookie) {
            mUrl = url;
            mCookie = cookie;
        }

        @Override
        protected String doInBackground(Void... params) {



            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(mUrl);

            String TAG ="ListRequest:";

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

            qrCodeReaderView.startCamera();
        }
    }
}
