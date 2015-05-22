package com.example.hukkei.roomapplication;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hukkei on 5/23/2015.
 */
public class LoadReservations extends AsyncTask<Void, Void, Boolean> {
    private static final String ROOM_STATUS_URL = "http://roomappgu.bitnamiapp.com/roomapp/findroombydate.php";

    //JSON IDS:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_RESERV_ID = "reserv_id";
    private static final String TAG_START_TIME = "start_time";
    private static final String TAG_END_TIME = "end_time";
    private static final String TAG_ID_USERS = "id_user";
    private static final String TAG_ID_ROOM = "id_room";
    private static final String TAG_RESERVATIONS = "reservations";

    //An array of all of our comments
    private JSONArray mReservations = null;
    //manages all of our comments in a list.
    private ArrayList<HashMap<String, String>> mReservationList;
    private String currentRoom;
    private FragmentManager fm;
    final ArrayList<CharSequence> twentyfourtimes = new ArrayList<CharSequence>();
    public LoadReservations(String currentRoom, FragmentManager fm){
        this.currentRoom = currentRoom;
        this.fm = fm;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
    @Override
    protected Boolean doInBackground(Void... arg0) {
        updateJSONdata();
        fixTimeList();
        Log.d("fel på koden eller", twentyfourtimes.get(0).toString());
        return null;

    }


    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        updateList();

    }


    public void updateJSONdata() {
        // Instantiate the arraylist to contain all the JSON data.
        // we are going to use a bunch of key-value pairs, referring
        // to the json element name, and the content, for example,
        // message it the tag, and "I'm awesome" as the content..

        mReservationList = new ArrayList<HashMap<String, String>>();

        // Bro, it's time to power up the J parser
        JSONParser jParser = new JSONParser();
        // Feed the beast our comments url, and it spits us
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        Date date = new Date();
        String today = dateFormat.format(date);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("the_day1", today+"00:00:00"));
        params.add(new BasicNameValuePair("the_day2", today+"23:59:59"));
        params.add(new BasicNameValuePair("the_room", currentRoom));
        //back a JSON object.  Boo-yeah Jerome.
        JSONObject json = jParser.makeHttpRequest(ROOM_STATUS_URL, "POST", params);

        //when parsing JSON stuff, we should probably
        //try to catch any exceptions:
        try {

            //I know I said we would check if "Posts were Avail." (success==1)
            //before we tried to read the individual posts, but I lied...
            //mComments will tell us how many "posts" or comments are
            //available
            mReservations = json.getJSONArray(TAG_RESERVATIONS);

            // looping through all posts according to the json object returned
            for (int i = 0; i < mReservations.length(); i++) {
                JSONObject c = mReservations.getJSONObject(i);

                //gets the content of each tag
                String reservid = c.getString(TAG_RESERV_ID);
                String starttime = c.getString(TAG_START_TIME);
                String endtime = c.getString(TAG_END_TIME);
                String userid = c.getString(TAG_ID_USERS);
                String roomid = c.getString(TAG_ID_ROOM);

                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_RESERV_ID, reservid);
                map.put(TAG_START_TIME, starttime);
                map.put(TAG_END_TIME, endtime);
                map.put(TAG_ID_USERS, userid);
                map.put(TAG_ID_ROOM, roomid);




                // adding HashList to ArrayList
                mReservationList.add(map);

                //annndddd, our JSON data is up to date same with our array list
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void updateList() {
        alertDialog aDialog;
        aDialog = new alertDialog();
        aDialog.setCurrentRoom(currentRoom);
        aDialog.setTimesList(twentyfourtimes);
        aDialog.show(fm, "alert Dialog");




    }

    public void fixTimeList(){
        twentyfourtimes.add("00:00:00");
        twentyfourtimes.add("01:00:00");
        twentyfourtimes.add("02:00:00");
        twentyfourtimes.add("03:00:00");
        twentyfourtimes.add("04:00:00");
        twentyfourtimes.add("05:00:00");
        twentyfourtimes.add("06:00:00");
        twentyfourtimes.add("07:00:00");
        twentyfourtimes.add("08:00:00");
        twentyfourtimes.add("09:00:00");
        twentyfourtimes.add("10:00:00");
        twentyfourtimes.add("11:00:00");
        twentyfourtimes.add("12:00:00");
        twentyfourtimes.add("13:00:00");
        twentyfourtimes.add("14:00:00");
        twentyfourtimes.add("15:00:00");
        twentyfourtimes.add("16:00:00");
        twentyfourtimes.add("17:00:00");
        twentyfourtimes.add("18:00:00");
        twentyfourtimes.add("19:00:00");
        twentyfourtimes.add("20:00:00");
        twentyfourtimes.add("21:00:00");
        twentyfourtimes.add("22:00:00");
        twentyfourtimes.add("23:00:00");
        for(int i=0; i<mReservationList.size(); i++){
            String[] justhours = mReservationList.get(i).get(TAG_START_TIME).split(" "); //split date string at blankspace so we get HH:mm:ss
            for(int j=0; j<twentyfourtimes.size();j++){
                if(justhours[1].equals(twentyfourtimes.get(j))){
                    twentyfourtimes.remove(j);
                }
            }


        }


    }


}