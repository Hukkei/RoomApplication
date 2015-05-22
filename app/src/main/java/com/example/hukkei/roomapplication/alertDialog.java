package com.example.hukkei.roomapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

public class alertDialog extends DialogFragment {

    ArrayList mSelectedItems;

    Context pappaContext;
    String currentRoom;
    ArrayList<CharSequence> twentyfourtimes = new ArrayList<CharSequence>();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        pappaContext = this.getActivity();
        final ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();  // Where we track the selected items
        final SharedPreferences sp = pappaContext.getSharedPreferences("loginSaved", Context.MODE_PRIVATE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //add times to array




        //builder.setAdapter(new MyAdapter(twentyfourtimes, mSelectedItems), null);

        // Set the dialog title
        builder.setTitle("Pick time")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(twentyfourtimes.toArray(new CharSequence[twentyfourtimes.size()]), null,
                        new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {


                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }


                            }
                        })
                        // Set the action buttons
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog


                        for(int i=0; i < mSelectedItems.size(); i++){

                            String selectedtime = twentyfourtimes.get(mSelectedItems.get(i)).toString();
                            AddReservation addit = new AddReservation(pappaContext,selectedtime,sp.getString("userid", null),currentRoom);
                            addit.execute();
                        }

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });


        return builder.create();
    }


    public void setCurrentRoom(String currentRoom) {
        this.currentRoom = currentRoom;
    }


    public void setTimesList(ArrayList<CharSequence> timeslist) {
        twentyfourtimes = new ArrayList<CharSequence>(timeslist);
    }
}
