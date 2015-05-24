package com.example.hukkei.roomapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

    class RemoveReservation extends AsyncTask<String, String, String> {

        //php url
        private static final String REMOVE_URL = "http://roomappgu.bitnamiapp.com/roomapp/removereservation.php";

        //JSON element ids from repsonse of php script:
        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";
        private static final String TAG_RESERV_ID = "reserv_id";
        // Progress Dialog
        private ProgressDialog pDialog;
        // JSON parser class
        JSONParser jsonParser = new JSONParser();

        Context c;
        String reservid;

        public RemoveReservation(Context c, String reservid){
            this.c = c;
            this.reservid = reservid;
        }
        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(c);
            pDialog.setMessage("Attempting to remove..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            try {
                // Building Parameters

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("reserv_id", reservid));


                Log.d("request!", "starting");
                // make http request
                JSONObject json = jsonParser.makeHttpRequest(
                        REMOVE_URL, "POST", params);


                Log.d("removal attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {

                    Log.d("Removal successful", json.toString());

                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Removal failed", json.getString(TAG_MESSAGE));
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

            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(c, file_url, Toast.LENGTH_LONG).show();
            }


        }

    }

