package com.example.hukkei.roomapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class bookScreen extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_screen);
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

    public void bokaSida(View view) {
        Intent getBookScreenBokaIntent = new Intent(this, bookScreenBoka.class);

        startActivity(getBookScreenBokaIntent);

    }

    public void onMapscreen(View view) {
        Intent getMapScreenIntent = new Intent (this, mapscreen.class);

        startActivity(getMapScreenIntent);

    }
    public void avbokNow(View view) {
        startActivity(new Intent(this, AvbokScreen.class));

    }


}
