package com.example.jasonthai.project;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static double[] mylatlon;
    Button location;
    //
    private static final boolean DEBUG = false;
    private LocationManager mlocManager;
    private LocationListener mlocListener;
    public double glat=0.0, glon=0.0, galtitude=0.0;
    int locationReady=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //addListenerOnButton3();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick1(View v) {
        //Intent intent = new Intent(this, ListBuildingsActivity.class);
        //startActivity(intent);
    }
    public void onClick2(View v) {
        //Intent intent = new Intent(this, ListBuildingsActivity.class);
        //startActivity(intent);
    }public void onClick3(View v) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    /*void addListenerOnButton3() {
        location = (Button)findViewById(R.id.compassMap);
        location.setEnabled(false);
        mlocManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();
        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        if (DEBUG) locationReady=1;
        if (DEBUG) glat = 35.4444;
        if (DEBUG) glon = -119.4444;
        if (DEBUG) location = (Button) findViewById(R.id.compassMap);
        if (DEBUG) location.setEnabled(true);
        location.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick3(View arg0) {
                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                //startActivity(browserIntent);
                //if (Csub1Activity.nList >= Csub1Activity.MAX_LIST) {
                //    Toast.makeText(getBaseContext(), "Max items reached.", Toast.LENGTH_SHORT).show();
                //    return;
                //}
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    class MyLocationListener implements LocationListener {
        //@Override
        public void onLocationChanged(Location loc) {
            glat = loc.getLatitude();
            glon = loc.getLongitude();
            locationReady = 1;
            location = (Button) findViewById(R.id.compassMap);
            location.setEnabled(true);

            MainActivity.mylatlon = new double[2];
            MainActivity.mylatlon[0] = glat;
            MainActivity.mylatlon[1] = glon;
        }
        //@Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }
        //@Override
        public void onProviderDisabled(String provider) {
            Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
        }
        //@Override
        public void onProviderEnabled(String provider) {
            Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }
    }*/
}
