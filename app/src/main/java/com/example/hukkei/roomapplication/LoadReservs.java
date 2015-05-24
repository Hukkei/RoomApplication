package com.example.hukkei.roomapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by hukkei on 5/23/2015.
 */
public class LoadReservs extends AsyncTask<Void, Void, Boolean> {
    private ProgressDialog pDialog;
    //testing from a real server:
    private static final String ROOM_STATUS_URL = "http://roomappgu.bitnamiapp.com/roomapp/roomstatus.php";
    SharedPreferences sp;
    String[] sendableArray;
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
    private JSONArray mReservations = null;
    //manages all of our comments in a list.
    private ArrayList<HashMap<String, String>> mReservList;
    ArrayList<String> userReservations = new ArrayList<String>();
    ArrayList<String> reservID;
    Context c;
    ListView lv;
    ArrayAdapter<String> arad;
    public LoadReservs(Context c, ListView lv, SharedPreferences sp){
        this.c = c;
        this.lv = lv;
        this.sp = sp;
    }
    public void updateJSONdata() {

        mReservList = new ArrayList<HashMap<String, String>>();


        JSONParser jParser = new JSONParser();

        JSONObject json = jParser.getJSONFromUrl(ROOM_STATUS_URL);


        try {


            mReservations = json.getJSONArray(TAG_POSTS);

            // looping through all posts according to the json object returned
            for (int i = 0; i < mReservations.length(); i++) {
                JSONObject c = mReservations.getJSONObject(i);

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
                mReservList.add(map);

                //annndddd, our JSON data is up to date same with our array list
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> getReservID(){


        return reservID;
    }

    public void createUsefulList(){
        //method creates a list containing strings for each reservation found on the user. these strings will be used to fill the listview.
        ArrayList<String> roomNames = new ArrayList<String>(); //so  that we can give roomids string names in GUI
        roomNames.add("USB");
        roomNames.add("VAX");
        roomNames.add("PDP");
        roomNames.add("CRAY");
        roomNames.add("DONKEY KONG");
        roomNames.add("ZELDA");
        roomNames.add("TETRIS");
        roomNames.add("DBASE");
        roomNames.add("ERNA");
        roomNames.add("KERMIT");
        roomNames.add("SWITCH");
        roomNames.add("ROUTER");
        userReservations = new ArrayList<String>();
        reservID = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        Date today = new Date();
        for(int i=0; i < mReservList.size(); i++ ){
            if(mReservList.get(i).get(TAG_ID_USERS).equals(sp.getString("userid", null))){
                try{
                    date = sdf.parse(mReservList.get(i).get(TAG_END_TIME));
                    if(today.before(date)){
                        reservID.add(mReservList.get(i).get(TAG_RESERV_ID));
                        String s  = "Tid: " + mReservList.get(i).get(TAG_START_TIME) + " -- " + mReservList.get(i).get(TAG_END_TIME) + " Rum: " + roomNames.get(Integer.parseInt(mReservList.get(i).get(TAG_ID_ROOM)) - 1) + " ID:" + mReservList.get(i).get(TAG_RESERV_ID);
                        userReservations.add(s);
                    }


                } catch(ParseException pe){
                    pe.printStackTrace();
                }


            }
        }


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //pDialog = new ProgressDialog(c);
        //pDialog.setMessage("Loading reservations..");
        //pDialog.setIndeterminate(false);
        //pDialog.setCancelable(true);
        //pDialog.show();
    }
    @Override
    protected Boolean doInBackground(Void... arg0) {
        updateJSONdata();
        createUsefulList();

        return null;

    }


    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        //pDialog.dismiss();
        arad = new ArrayAdapter<String>(c,android.R.layout.simple_list_item_1, userReservations); //adapter for listview
        lv.setAdapter(arad);
        AvbokScreen avb = new AvbokScreen();
        avb.setListener(lv); //setlistner for the list.



    }
}