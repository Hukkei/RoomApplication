package com.example.hukkei.roomapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private EditText user, pass;
    private Button mSubmit;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    SharedPreferences sp;




    //php login script location:
    private static final String LOGIN_URL = "http://roomappgu.bitnamiapp.com/roomapp/login.php";

    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_USER_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = this.getSharedPreferences("loginSaved", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setup input fields
        user = (EditText)findViewById(R.id.editText);
        pass = (EditText)findViewById(R.id.editText2);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;

        }else if (id == R.id.avslutaApp){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onLoggaIn(View view) {
        new AttemptLogin().execute(); //executes async task for login



    }









    class AttemptLogin extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            String username = user.getText().toString();
            String password = pass.getText().toString();
            try {
                //create params

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");
                // do http request based on parameters
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);


                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    String userid = json.getString(TAG_USER_ID);
                    Log.d("Login Successful!", json.toString());
                    // save user data
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("username", username);
                    edit.putString("userid", userid);
                    edit.commit();
                    Intent i = new Intent(MainActivity.this, mainscreen.class);
                    finish();
                    //Start activity:mainscreen
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(MainActivity.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }

    }






