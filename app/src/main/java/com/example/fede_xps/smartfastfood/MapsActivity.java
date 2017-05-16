package com.example.fede_xps.smartfastfood;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fede_xps.smartfastfood.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private String cookieUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        cookieUser = getIntent().getExtras().getString("cookie");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera

        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(MapsActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION );
                        }
                    })
                    .create()
                    .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }

        mMap.setMyLocationEnabled(true);

        MapsTask mt = new MapsTask();
        mt.execute((Void) null);
    }
    public void onSearch(View view){

        EditText location_tf = (EditText)findViewById(R.id.TFaddress);
        String location= location_tf.getText().toString();
        List<Address> addressList=null;

        if(location!=null || !location.equals("")){
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location,1);


            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address= addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));



        }
    }

    public void changeType(View view){

        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        }
        else{
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    public void onZoom(View view){
        if(view.getId() == R.id.Bzoomin){
            mMap.animateCamera(CameraUpdateFactory.zoomIn());

        }
        if(view.getId() == R.id.Bzoomout){
            mMap.animateCamera(CameraUpdateFactory.zoomOut());


        }

    }

    @Override
    public boolean onMarkerClick(final Marker arg0) {
        Log.d("Fanculo", "Javaaaa");
        arg0.showInfoWindow();
        //Toast.makeText(MapsActivity.this, "ZIOOOPORCONEEEE!!!!!!!!", Toast.LENGTH_SHORT).show();  // display toast
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        String url = "http://smartfastfood-nikkolo94.c9users.io/list/"+marker.getTag();
        ListRequestTask lr = new ListRequestTask(url, cookieUser);
        lr.execute( (Void) null);

    }


    public class MapsTask extends AsyncTask<Void, Void, String> {




        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.



            HttpClient httpClient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet("http://smartfastfood-nikkolo94.c9users.io/maps");

            String TAG ="MAPS";


            try {
                String lat = "41.895165";
                String lng = "12.493417";
                httpGet.addHeader("lat", lat);
                httpGet.addHeader("lng", lng);

                HttpResponse response = httpClient.execute(httpGet);

                int statusCode= response.getStatusLine().getStatusCode();

                if( statusCode != 200 ) {
                    return "Errore server!";
                    //Log.d(TAG, statusCode+"");
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

            startMarker(s);

        }

        @Override
        protected void onCancelled() {

        }
    }

    private void startMarker(String s) {

        JSONArray jArray = null;

        try {
            jArray = new JSONArray(s);

            if (jArray != null) {
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jo = jArray.getJSONObject(i);
                    double lat = jo.getDouble("lat");
                    double lng = jo.getDouble("lng");
                    int owner_id = jo.getInt("owner_id");
                    LatLng ll = new LatLng(lat, lng);
                    Marker mark = mMap.addMarker(new MarkerOptions().position(ll).title("mecellaporco"));
                    mark.setTag(owner_id);
                }
            }

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + jArray + "\"");
        }
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

        }
    }

    private void creaLista(String s) {

        Intent start = new Intent(MapsActivity.this, ListActivity.class);
        start.putExtra("json", s);
        start.putExtra("token", cookieUser);
        startActivity(start);

    }


}
