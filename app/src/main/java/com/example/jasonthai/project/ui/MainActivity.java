package com.example.jasonthai.project.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import android.view.View.OnClickListener;

import com.example.jasonthai.project.Manifest;
import com.example.jasonthai.project.R;
import com.example.jasonthai.project.shared.Utils;
import com.example.jasonthai.project.service.UtilityService;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final int PERMISSION_REQ = 0;

    public static double[] mylatlon;
    TextView location;
    //
    private static final boolean DEBUG = false;
    private LocationManager mlocManager;
    private LocationListener mlocListener;
    public double glat = 0.0, glon = 0.0, galtitude = 0.0;
    int locationReady = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check fine location permission has been granted
        if (!Utils.checkFineLocationPermission(this)) {
            // See if user has denied permission in the past
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show a simple snackbar explaining the request instead
                //showPermissionSnackbar();
            } else {
                // Otherwise request permission from user
                if (savedInstanceState == null) {
                    requestFineLocationPermission();
                }
            }
        } else {
            // Otherwise permission is granted (which is always the case on pre-M devices)
            fineLocationPermissionGranted();
        }

        //ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        addListenerOnButton3();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(
                        view, R.string.permission_explanation, Snackbar.LENGTH_LONG)
                        .setAction(R.string.permission_explanation_action, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestFineLocationPermission();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        UtilityService.requestLocation(this);
    }

    public void onClick(View v) {
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
        if (id == R.id.action_AboutUs) {
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick1(View v) {
        Intent intent = new Intent(this, EditCarInfoActivity.class);
        startActivity(intent);
    }

    public void onClick2(View v) {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }

    public void onClick3(View v) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    void addListenerOnButton3() {
        location = (TextView) findViewById(R.id.compassMap);
        location.setEnabled(true);
        mlocManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();

        //mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        if (DEBUG) locationReady = 1;
        if (DEBUG) glat = 35.4444;
        if (DEBUG) glon = -119.4444;
        if (DEBUG) location = (TextView) findViewById(R.id.compassMap);
        if (DEBUG) location.setEnabled(true);

        MainActivity.mylatlon = new double[2];
        MainActivity.mylatlon[0] = glat;
        MainActivity.mylatlon[1] = glon;

        //location.setOnClickListener(new OnClickListener() {
            //@Override
        //    public void onClick(View arg0) {
        //        Intent intent = new Intent();
        //        intent.setClass(getApplicationContext(), MapsActivity.class);
        //        startActivity(intent);
        //    }
        //});

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Search for gas stations nearby
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=gas stations");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }

    class MyLocationListener implements LocationListener {
        //@Override
        public void onLocationChanged(Location loc) {
            glat = loc.getLatitude();
            glon = loc.getLongitude();
            locationReady = 1;
            location = (TextView) findViewById(R.id.compassMap);
            location.setEnabled(true);

            MainActivity.mylatlon = new double[2];
            MainActivity.mylatlon[0] = glat;
            MainActivity.mylatlon[1] = glon;
        }

        //@Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        //@Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }

        //@Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }
    }

    //public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    //    switch (requestCode) {
    //        case 1: {
                // If request is cancelled, the result arrays are empty.
    //            if (grantResults.length > 0
    //                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
    //                mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

    //            } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
    //            }
    //            return;
    //        }
            // other 'case' lines to check for other
            // permissions this app might request
    //    }
    //}
    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Permissions request result callback
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQ:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fineLocationPermissionGranted();
                }
        }
    }
    /**
     * Request the fine location permission from the user
     */
    private void requestFineLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQ);
    }

    /**
     * Run when fine location permission has been granted
     */
    private void fineLocationPermissionGranted() {
        UtilityService.addGeofences(this);
        UtilityService.requestLocation(this);
    }

    /**
     * Show a permission explanation snackbar
     */
    private void showPermissionSnackbar() {
        Snackbar.make(
                findViewById(R.id.container), R.string.permission_explanation, Snackbar.LENGTH_LONG)
                .setAction(R.string.permission_explanation_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestFineLocationPermission();
                    }
                })
                .show();
    }
}
