package com.example.jasonthai.project;

import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private LocationManager mlocManager;
    private LocationListener mlocListener;
    public double glat=0.0, glon=0.0, galtitude=0.0;
    int locationReady=0;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //LatLng location = new LatLng(MainActivity.mylatlon[0], MainActivity.mylatlon[1]);
        //mMap.addMarker(new MarkerOptions().position(location).title("Marker in Location"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    class MyLocationListener implements android.location.LocationListener {
        //@Override
        public void onLocationChanged(Location loc) {
            glat = loc.getLatitude();
            glon = loc.getLongitude();
            locationReady = 1;

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
    }
}
