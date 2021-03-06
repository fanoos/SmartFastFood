package com.example.fede_xps.smartfastfood;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private FacebookUserLoginTask mAuthTask2 = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private String id;
    private String token;

    protected String ret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken!=null) {
            LoginManager.getInstance().logOut();
        }
        callbackManager = CallbackManager.Factory.create();

        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                showProgress(true);

                Log.d("DATI", loginResult.getAccessToken().getUserId() + "  "+ loginResult.getAccessToken().getToken());




                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                String email=null;
                                String name =null;
                                try {
                                    email = object.getString("email");
                                    name = object.getString("name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                goFacebook(email, name);
                            }
                        });

                id = loginResult.getAccessToken().getUserId();
                token = loginResult.getAccessToken().getToken();

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();



            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });


        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailView = (EditText) findViewById(R.id.email);




        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });



        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button RegisterButton = (Button) findViewById(R.id.register);
        RegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });



        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


/*
        Button b = (Button) findViewById(R.id.debug);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(LoginActivity.this, ListActivity.class);
                start.putExtra("json", "[{\"id\":2,\"owner_id\":1,\"name\":\"pizza\",\"price\":3,"+
                "\"image\":\"http:\\/\\/www.neuromed.it\\/wp-content\\/uploads\\/2016\\/07\\/pasta.jpg\"}"+
                ",{\"id\":3,\"owner_id\":1,\"name\":\"carne\",\"price\":10,\"image\":\"http:\\/\\/"+
                "www.my-personaltrainer.it\\/images\\/tag\\/Secondi_di_carne.jpg\"}]");
                start.putExtra("token", "CndRKpicESxTOPClD4zlAg7pVBN9cOOS3unneQvh0sFnROmcejRNNqd3A11V");
                startActivity(start);
            }
        });
        */


        /*
        String s = getIntent().getExtras().getString("message");
        if(s != null)
            Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG);
        */
    }

    private void attemptRegister() {

        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.putExtra("email", mEmailView.getText());
        startActivity(intent);

    }

    protected void goFacebook(String email, String name) {
        Log.d("FACEBOOK", email+" "+name);
        mAuthTask2 = new FacebookUserLoginTask(id, token, email, name);
        mAuthTask2.execute( (Void) null);
    }


    protected void Stampa(String s) {

        mPasswordView.setText("");
        Toast.makeText(LoginActivity.this,
                s , Toast.LENGTH_LONG).show();
    }

    public void debugList() {
        String s = "[{id:1,owner_id:1,name:paninazzo,price:2,image:null},{id:1,owner_id:1,name:paninazzone,price:2,image:null}]";
        Intent start = new Intent(LoginActivity.this, ListActivity.class);
        start.putExtra("json", s);
        startActivity(start);
    }

    protected void startHome(String s) {

        JSONObject js=null;
        try {
            js = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String type = null;
        Intent open = null;
        try {
            type=js.getString("type");
            Log.d("RETURN", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(type.equals("user")) {

            open = new Intent(LoginActivity.this, HomeLoginActivity.class);
            Log.d("LOGIN", "client");


        } else if(type.equals("employ")) {

            open = new Intent(LoginActivity.this, HomeEmployActivity.class);
            try {
                open.putExtra("id", js.getString("mid"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("LOGIN", "employ");

        } else {

            open = new Intent(LoginActivity.this, VendorActivity.class);
            try {
                open.putExtra("id", js.getString("mid"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("LOGIN", "vendor");

        }

        try {
            open.putExtra("cookie", js.getString("token"));
            Log.d("LOGIN", js.getString("token"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        showProgress(false);
        startActivity(open);

    }


    //qua ritorna il risultato di facebook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }



    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();



        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);


        }
    }




    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        //mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.



            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://smartfastfood-nikkolo94.c9users.io/appLogin");

            String TAG ="lol";

            try {
                httpGet.addHeader("email", mEmail);
                httpGet.addHeader("password", mPassword);


                HttpResponse response = httpClient.execute(httpGet);

                int statusCode= response.getStatusLine().getStatusCode();
                if( statusCode != 200 ) {
                    return "Errore server!";
                }

                final String responseBody = EntityUtils.toString(response.getEntity());
                Log.d(TAG, "Signed in as: " + responseBody);

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
            mAuthTask = null;
            showProgress(false);
            if(s.equals("Your Password Must Contain At Least 8 Characters!")||
                    s.equals("Your Password Must Contain At Least 1 Number!")||
                    s.equals("Your Password Must Contain At Least 1 Capital Letter!")||
                    s.equals("Your Password Must Contain At Least 1 Lowercase Letter!")||
                    s.equals("no, password") || s.equals("no, email"))
                Stampa(s);
            else
                startHome(s);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public class FacebookUserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mEmail;
        private final String mToken;
        private final String mId;
        private final String mName;

        FacebookUserLoginTask(String id, String token, String email, String name) {
            mEmail = email;
            mToken=token;
            mId=id;
            mName=name;

        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.



            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://smartfastfood-nikkolo94.c9users.io/FacLogin");

            String TAG ="FACEBOOK LOGIN";

            try {
                httpGet.addHeader("email", mEmail);
                httpGet.addHeader("atoken", mToken);
                httpGet.addHeader("fid", mId);
                httpGet.addHeader("name", mName);

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
            mAuthTask2 = null;
            showProgress(false);

            Log.d("RESULT", s);
            startHome(s);

        }

        @Override
        protected void onCancelled() {
            mAuthTask2 = null;
            showProgress(false);
        }
    }



}

