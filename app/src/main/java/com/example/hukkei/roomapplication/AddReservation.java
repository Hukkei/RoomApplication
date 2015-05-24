package com.example.hukkei.roomapplication;

/**
 * Created by hukkei on 5/13/2015.
 */


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


  public class AddReservation extends AsyncTask<String, String, String> {

        private Button  mSubmit;
        private String thedate, uid, rid;
        private Context c;
        // Progress Dialog
        private ProgressDialog pDialog;
        // JSON parser class
        JSONParser jsonParser = new JSONParser();

        private static final String POST_COMMENT_URL = "http://roomappgu.bitnamiapp.com/roomapp/addreservation.php";
        //ids
        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";
        public AddReservation(Context c, String date, String uid, String rid){
            this.c = c;
            this.thedate = date;
            this.uid = uid;
            this.rid = rid;


        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(c);
            pDialog.setMessage("Making reservation...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
            Date date = new Date();
            String startingtime = df.format(date) + thedate;
            //we create a second date that is 1hour after the initial date, this will be the enddate in the database.
            String reservation_starttime = startingtime ;
            String endtimestring= "mistakes were made";
            try {
                DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date endtime = df2.parse(startingtime);
                Calendar cal = GregorianCalendar.getInstance();
                cal.setTime(endtime);
                cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
                endtimestring = df2.format(cal.getTime());

            } catch(ParseException pe) {
                pe.printStackTrace();
            }
            String reservation_endtime = endtimestring;
            String reservation_roomid = rid;
            String reservation_userid = uid;



            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("start_time", reservation_starttime));
                params.add(new BasicNameValuePair("end_time", reservation_endtime));
                params.add(new BasicNameValuePair("id_room", reservation_roomid));
                params.add(new BasicNameValuePair("id_user", reservation_userid));

                Log.d("request!", "starting");

                //make httprequest with parameters
                JSONObject json = jsonParser.makeHttpRequest(
                        POST_COMMENT_URL, "POST", params);

                // full json response
                Log.d("reservation attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Reservation added!", json.toString());

                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Reservation failure", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(c, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }





