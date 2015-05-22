package com.example.hukkei.roomapplication;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class bookScreenBoka extends ActionBarActivity {

    Button btn;
    Button btnTid;

    int year;
    int month;
    int day;
    int hour;
    int minute;

    static final int dialog_id =1;
    static final int dialog_tid = 2;

    private TextView textout;
    private TextView txtOutput2;




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.book_screen_boka);

            final Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
            hour = cal.get(Calendar.HOUR_OF_DAY);
            minute = cal.get(Calendar.MINUTE);


            showDialogOnBtn();
            showbDialogTid();
        }


//  tid

    public void showbDialogTid (){

        btnTid = (Button)findViewById(R.id.btn_tid);
        btnTid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(dialog_tid);
            }
        });

    }


    protected TimePickerDialog.OnTimeSetListener kTimePickLis =
            new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int h, int min) {

            hour = h;
            minute = min;

            String omvandlaTime = String.format("%02d:%02d", hour, minute);

            Toast.makeText(bookScreenBoka.this,"Du har valt tiden: " + omvandlaTime,Toast.LENGTH_SHORT).show();
            textout = (TextView) findViewById(R.id.txtOutput);

            textout.setText("Du har valt tiden: " + omvandlaTime);
            textout.setKeyListener(null);


        }
    };



// Slut tid


//  datum

    public void showDialogOnBtn(){

        btn = (Button)findViewById(R.id.button_datepick);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog(dialog_id);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        AlertDialog mDialog = null;

        switch (id){
            case dialog_id:
                mDialog = new DatePickerDialog(this, dpickerListner, year, month, day);
                break;

            case dialog_tid:
                mDialog = new TimePickerDialog(bookScreenBoka.this,kTimePickLis, hour, minute,true);
                break;
        }

        return mDialog;
    }

    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int y, int m, int d) {

            year = y;
            month = m + 1;
            day = d;


            Toast.makeText(bookScreenBoka.this,"Du har valt datum: " + day + "/" + month + "/" + year,Toast.LENGTH_SHORT).show();
            txtOutput2 = (TextView) findViewById(R.id.txtOutputTid);

            txtOutput2.setText("Du har valt datum: " + day + "/" + month + "/" + year);
            txtOutput2.setKeyListener(null);

        }
    };

    //slut datum


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

    }
