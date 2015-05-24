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
    //phpurl
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

    //jsonarray for reservations
    private JSONArray mReservations = null;
    //array for reservations
    private ArrayList<HashMap<String, String>> mReservationList;
    private String currentRoom; //saves value of which room was clicked
    private FragmentManager fm;
    final ArrayList<CharSequence> twentyfourtimes = new ArrayList<CharSequence>();//Array that contains strings for each our in a day e.g. 01:00:00 or 14:00:00, these will be displayed in alertdialog
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
        Log.d("check", twentyfourtimes.get(0).toString());
        return null;

    }


    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        updateList();

    }


    public void updateJSONdata() {

        //instansiate the list
        mReservationList = new ArrayList<HashMap<String, String>>();

        // Bro, it's time to power up the J parser
        JSONParser jParser = new JSONParser();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        Date date = new Date();
        String today = dateFormat.format(date);
        //now we create a list that contains the the id of the room we are opening, the a date for today at 00:00:00 and a date for today at 23:59:59
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("the_day1", today+"00:00:00"));
        params.add(new BasicNameValuePair("the_day2", today+"23:59:59"));
        params.add(new BasicNameValuePair("the_room", currentRoom));
        //make httprequest with given params.
        JSONObject json = jParser.makeHttpRequest(ROOM_STATUS_URL, "POST", params);


        try {


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
        //add times to arraylist
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
            //since the value we get for starttime from the database is a full datetime string like 2000-05-04 22:20:10,
            // and we only want to get the time, we will split the string at blankspace.
            String[] justhours = mReservationList.get(i).get(TAG_START_TIME).split(" "); //split date string at blankspace so we get HH:mm:ss
            for(int j=0; j<twentyfourtimes.size();j++){ //here we remove all occurences of a specific time in our twentyfourtimes list. this list will be displayed in alertdialog
                if(justhours[1].equals(twentyfourtimes.get(j))){
                    twentyfourtimes.remove(j);
                }
            }


        }


    }


}