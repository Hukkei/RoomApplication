package com.example.hukkei.roomapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by simon on 2015-05-13.
 */



public class mapscreen extends ActionBarActivity {



    private ProgressDialog pDialog;
    //testing from a real server:
    private static final String ROOM_STATUS_URL = "http://roomappgu.bitnamiapp.com/roomapp/roomstatus.php";

    //JSON IDS:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_RESERV_ID = "reserv_id";
    private static final String TAG_START_TIME = "start_time";
    private static final String TAG_END_TIME = "end_time";
    private static final String TAG_ID_USERS = "id_user";
    private static final String TAG_ID_ROOM = "id_room";
    //An array of all of our comments
    private JSONArray mRooms = null;
    //manages all of our comments in a list.
    private ArrayList<HashMap<String, String>> mRoomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapscreen);
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

        }else if (id == R.id.avslutaApp){ //avsluta appen
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //loading the comments via AsyncTask
        new LoadRooms().execute();
    }


    /**
     * Retrieves json data of comments
     */
    public void updateJSONdata() {
// Instantiate the arraylist to contain all the JSON data.
        // we are going to use a bunch of key-value pairs, referring
        // to the json element name, and the content, for example,
        // message it the tag, and "I'm awesome" as the content..

        mRoomList = new ArrayList<HashMap<String, String>>();

        // Bro, it's time to power up the J parser
        JSONParser jParser = new JSONParser();
        // Feed the beast our comments url, and it spits us
        //back a JSON object.  Boo-yeah Jerome.
        JSONObject json = jParser.getJSONFromUrl(ROOM_STATUS_URL);

        //when parsing JSON stuff, we should probably
        //try to catch any exceptions:
        try {

            //I know I said we would check if "Posts were Avail." (success==1)
            //before we tried to read the individual posts, but I lied...
            //mComments will tell us how many "posts" or comments are
            //available
            mRooms = json.getJSONArray(TAG_POSTS);

            // looping through all posts according to the json object returned
            for (int i = 0; i < mRooms.length(); i++) {
                JSONObject c = mRooms.getJSONObject(i);

                //gets the content of each tag
                String reservid = c.getString(TAG_RESERV_ID);
                String starttime = c.getString(TAG_START_TIME);
                String endtime = c.getString(TAG_END_TIME);
                String idusers = c.getString(TAG_ID_USERS);
                String idroom = c.getString(TAG_ID_ROOM);



                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_RESERV_ID, reservid);
                map.put(TAG_START_TIME, starttime);
                map.put(TAG_END_TIME, endtime);
                map.put(TAG_ID_USERS, idusers);
                map.put(TAG_ID_ROOM, idroom);


                // adding HashList to ArrayList
                mRoomList.add(map);

                //annndddd, our JSON data is up to date same with our array list
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts the parsed data into our listview
     */
    private void updateList() {
// For a ListActivity we need to set the List Adapter, and in order to do
        //that, we need to create a ListAdapter.  This SimpleAdapter,
        //will utilize our updated Hashmapped ArrayList,
        //use our single_post xml template for each item in our list,
        //and place the appropriate info from the list to the
        //correct GUI id.  Order is important here.
        ArrayList<Button> theRooms = new ArrayList<Button>();
        theRooms.add((Button) findViewById(R.id.usb));
        theRooms.add((Button) findViewById(R.id.vax));
        theRooms.add((Button) findViewById(R.id.pdp));
        theRooms.add((Button) findViewById(R.id.cray));
        theRooms.add((Button) findViewById(R.id.donkey));
        theRooms.add((Button) findViewById(R.id.zelda));
        theRooms.add((Button) findViewById(R.id.tetris));
        theRooms.add((Button) findViewById(R.id.dbase));
        theRooms.add((Button) findViewById(R.id.erna));
        theRooms.add((Button) findViewById(R.id.kermit));
        theRooms.add((Button) findViewById(R.id.switch1));
        theRooms.add((Button) findViewById(R.id.router));

        for(int i=0; i<theRooms.size(); i++){ //check if todays date and time occurs in any reservation and changes button color if it does
            for(int j=0; j<mRoomList.size(); j++){
                if(Integer.parseInt(mRoomList.get(j).get(TAG_ID_ROOM))== i + 1){

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    try{
                        Date startdate = sdf.parse(mRoomList.get(j).get(TAG_START_TIME));
                        Calendar cal = GregorianCalendar.getInstance();
                        cal.setTime(startdate);
                        cal.add(Calendar.MINUTE, 59);
                        cal.add(Calendar.SECOND, 59);
                        Date enddate = cal.getTime();
                        Log.d("time:", startdate.toString() +"-----" + enddate.toString() + "roomid:" + mRoomList.get(j).get(TAG_ID_ROOM));
                        if(startdate.compareTo(date) * date.compareTo(enddate) > 0 ){
                            theRooms.get(i).setBackgroundResource(R.drawable.button_blue);
                            theRooms.get(i).setTextColor(getResources().getColor(R.color.white));
                            break;
                        } else {
                            theRooms.get(i).setBackgroundResource(R.drawable.button_white);
                            theRooms.get(i).setTextColor(getResources().getColor(R.color.black));
                        }
                    } catch(ParseException pe){
                        pe.printStackTrace();
                    }

                }
            }
        }







    }

    public void reservTime(View view) {
        LoadReservations lr;
        switch (view.getId()) {

            case (R.id.usb):
                lr = new LoadReservations("1",getFragmentManager());
                lr.execute();
                break;
            case (R.id.vax):
                lr = new LoadReservations("2",getFragmentManager());
                lr.execute();
                break;
            case (R.id.pdp):
                lr = new LoadReservations("3",getFragmentManager());
                lr.execute();
                break;
            case (R.id.cray):
                lr = new LoadReservations("4",getFragmentManager());
                lr.execute();
                break;
            case (R.id.donkey):
                lr = new LoadReservations("5",getFragmentManager());
                lr.execute();
                break;
            case (R.id.zelda):
                lr = new LoadReservations("6",getFragmentManager());
                lr.execute();
                break;
            case (R.id.tetris):
                lr = new LoadReservations("7",getFragmentManager());
                lr.execute();
                break;
            case (R.id.dbase):
                lr = new LoadReservations("8",getFragmentManager());
                lr.execute();
                break;
            case (R.id.erna):
                lr = new LoadReservations("9",getFragmentManager());
                lr.execute();
                break;
            case (R.id.kermit):
                lr = new LoadReservations("10",getFragmentManager());
                lr.execute();
                break;
            case (R.id.switch1):
                lr = new LoadReservations("11",getFragmentManager());
                lr.execute();
                break;
            case (R.id.router):
                lr = new LoadReservations("12",getFragmentManager());
                lr.execute();
                break;


        }
    }

    public void refreshRooms(View v){
        new LoadRooms().execute();
    }
    public class LoadRooms extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mapscreen.this);
            pDialog.setMessage("Loading rooms...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... arg0) {
            updateJSONdata();
            return null;

        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            updateList();
        }
    }


}





















