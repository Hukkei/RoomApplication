package com.example.hukkei.roomapplication;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
public class AvbokScreen extends Activity implements AdapterView.OnItemClickListener {

    SharedPreferences sp;
    ArrayAdapter<String> arad;
    ListView lv;
    LoadReservs lr;


    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.avbokscreen);
        lv = (ListView) findViewById(R.id.listview1);
        sp = this.getSharedPreferences("loginSaved", Context.MODE_PRIVATE);
        lr = new LoadReservs(this, lv, sp);
        lr.execute();

        //Log.d("detfunkaryo", idPositions.get(0));



    }
    @Override
    public void onResume(){
        super.onResume();

    }

    public void setListener(ListView lv){
        lv.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tv = (TextView) view;

        String[] resID = tv.getText().toString().split("ID:");
        RemoveReservation rr = new RemoveReservation(parent.getContext(),resID[1]);
        rr.execute();



    }
}
