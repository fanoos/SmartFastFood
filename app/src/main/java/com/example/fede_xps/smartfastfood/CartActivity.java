package com.example.fede_xps.smartfastfood;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalInvoiceData;
import com.paypal.android.MEP.PayPalPayment;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements View.OnClickListener{


    private static final int PAYPAL_BUTTON_ID = 50 ;
    int total;
    private CheckoutButton launchPayPalButton;
    private boolean _paypalLibraryInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Bundle extra = getIntent().getBundleExtra("extra");
        ArrayList<String> list = (ArrayList<String>) extra.getSerializable("list");

        Log.d("qui",list.toString());

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

        initLibrary();
        showPayPalButton();

    }


    public void initLibrary() {
        PayPal pp = PayPal.getInstance();

        if (pp == null) {  // Test to see if the library is already initialized

            // This main initialization call takes your Context, AppID, and target server
            pp = PayPal.initWithAppID(this, "APP-80W284485P519543T", PayPal.ENV_SANDBOX);

            // Required settings:

            // Set the language for the library
            pp.setLanguage("en_US");

            // Some Optional settings:

            // Sets who pays any transaction fees. Value is:
            // FEEPAYER_SENDER, FEEPAYER_PRIMARYRECEIVER, FEEPAYER_EACHRECEIVER, and FEEPAYER_SECONDARYONLY
            pp.setFeesPayer(PayPal.FEEPAYER_EACHRECEIVER);

            // true = transaction requires shipping
            pp.setShippingEnabled(true);

            _paypalLibraryInit = true;
        }
    }

    @Override
    public void onClick(View arg0) {
        // Create a basic PayPal payment

        Log.d("PAYPAL", "Start paypal");
        PayPalPayment payment = new PayPalPayment();

        // Set the currency type
        payment.setCurrencyType("USD");

        // Set the recipient for the payment (can be a phone number)
        payment.setRecipient("baldoni@gmail.com");

        // Set the payment amount, excluding tax and shipping costs
        payment.setSubtotal(new BigDecimal(total));

        // Set the payment type--his can be PAYMENT_TYPE_GOODS,
        // PAYMENT_TYPE_SERVICE, PAYMENT_TYPE_PERSONAL, or PAYMENT_TYPE_NONE
        payment.setPaymentType(PayPal.PAYMENT_TYPE_GOODS);

        // PayPalInvoiceData can contain tax and shipping amounts, and an
        // ArrayList of PayPalInvoiceItem that you can fill out.
        // These are not required for any transaction.
        PayPalInvoiceData invoice = new PayPalInvoiceData();

        // Set the tax amount
        invoice.setTax(new BigDecimal(10));


        Intent checkoutIntent = PayPal.getInstance().checkout(payment,this);
        this.startActivityForResult(checkoutIntent, 1);
    }

    private void showPayPalButton() {

        // Generate the PayPal checkout button and save it for later use
        PayPal pp = PayPal.getInstance();
        launchPayPalButton = pp.getCheckoutButton(this, PayPal.BUTTON_278x43, CheckoutButton.TEXT_PAY);

        // The OnClick listener for the checkout button
        launchPayPalButton.setOnClickListener(this);

        // Add the listener to the layout
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams (RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.bottomMargin = 10;
        launchPayPalButton.setLayoutParams(params);
        launchPayPalButton.setId(R.id.id_paypal);
        ((RelativeLayout) findViewById(R.id.RelativeLayout01)).addView(launchPayPalButton);
        ((RelativeLayout) findViewById(R.id.RelativeLayout01)).setGravity(Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        Log.d("PAYPAL", "result"+resultCode);
        switch (resultCode) {
            // The payment succeeded
            case Activity.RESULT_OK:
                String payKey = intent.getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
                this.paymentSucceeded(payKey);
                break;

            // The payment was canceled
            case Activity.RESULT_CANCELED:
                this.paymentCanceled();
                break;

            // The payment failed, get the error from the EXTRA_ERROR_ID and EXTRA_ERROR_MESSAGE
            case PayPalActivity.RESULT_FAILURE:
                String errorID = intent.getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
                String errorMessage = intent.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
                this.paymentFailed(errorID, errorMessage);

            default:
                super.onActivityResult(requestCode, resultCode, intent);
        }

    }

    private void paymentFailed(String errorID, String errorMessage) {
        Log.d("PAYPAL", errorMessage);
    }

    private void paymentCanceled() {
        Log.d("PAYPAL", "cancel");
    }

    private void paymentSucceeded(String payKey) {
        Log.d("PAYPAL", payKey);

        Intent intent = new Intent(CartActivity.this, BookedActivity.class);
        startActivity(intent);
    }

    public class PaypalTask extends AsyncTask<Void, Void, String> {

        private final String mToken;

        PaypalTask(String token) {
            mToken=token;
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.



            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://smartfastfood-nikkolo94.c9users.io/");

            String TAG ="SendOrder";

            try {
                httpGet.addHeader("atoken", mToken);
                httpGet.addHeader("total", total+"");
                httpGet.addHeader("order", "");

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

        }

        @Override
        protected void onCancelled() {

        }
    }

}
