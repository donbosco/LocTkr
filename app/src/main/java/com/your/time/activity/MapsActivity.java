package com.your.time.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    private static final int MILLISECONDS_PER_SECOND = 1000;
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    private static final float FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

    LocationManager locationManager;
    private GoogleMap mMap;
    private double currentLatitude;
    private double currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        getLocation();
    }

    public Location getLocation() {
        Location loc = null;
        try {
            locationManager = (LocationManager) this
                    .getSystemService(LOCATION_SERVICE);
            // getting GPS status
            boolean checkGPS = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean checkNetwork = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!checkGPS && !checkNetwork) {
                Toast.makeText(this, "No Service Provider is available", Toast.LENGTH_SHORT).show();
                showSettingsAlert();
            } else {
                loc = getCurrentLocation(loc, checkGPS, checkNetwork);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loc;
    }

    @Nullable
    private Location getCurrentLocation(Location loc, boolean checkGPS, boolean checkNetwork) {
        // First get location from Network Provider
        if (checkNetwork) {
            //Toast.makeText(mContext, "Network", Toast.LENGTH_SHORT).show();

            try {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        UPDATE_INTERVAL,
                        FASTEST_INTERVAL, this);
                Log.d("Network", "Network");
                if (locationManager != null) {
                    loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    currentLatitude = loc.getLatitude();
                    currentLongitude = loc.getLongitude();
                    addMarker();
                }
            } catch (SecurityException e) {

            }
        }

        // if GPS Enabled get lat/long using GPS Services
        if (checkGPS) {
            //Toast.makeText(mContext, "GPS", Toast.LENGTH_SHORT).show();
            try {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        UPDATE_INTERVAL,
                        FASTEST_INTERVAL, this);
                Log.d("GPS Enabled", "GPS Enabled");
                if (locationManager != null) {
                    loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    currentLatitude = loc.getLatitude();
                    currentLongitude = loc.getLongitude();
                    addMarker();
                }
            } catch (SecurityException e) {

            }

        }
        return loc;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);


        alertDialog.setTitle("GPS Not Enabled");

        alertDialog.setMessage("Do you wants to turn On GPS");


        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                MapsActivity.this.startActivityForResult(intent, 1);
            }
        });


        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Location loc = new Location(LocationManager.GPS_PROVIDER);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            loc = getCurrentLocation(loc, true, false);
            currentLongitude = loc.getLongitude();
            currentLatitude = loc.getLatitude();
            addMarker();
        } else {
            loc = getLocation();
            //Toast.makeText(this,"No service Provider is available. GPS is still turned off.",Toast.LENGTH_SHORT).show();
        }
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
        //Toast.makeText(this, "Map Ready.", Toast.LENGTH_SHORT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "Connection Resumed.", Toast.LENGTH_SHORT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Toast.makeText(this, "Connected.", Toast.LENGTH_SHORT);
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Toast.makeText(this, "Connection Suspended.", Toast.LENGTH_SHORT);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Toast.makeText(this, "Connection Faild.", Toast.LENGTH_SHORT);
    }

    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(this, "Location Changed.", Toast.LENGTH_SHORT);

        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        addMarker();
    }

    private void addMarker() {
        // Add a marker in current location and move the camera
        LatLng currentLocation = new LatLng(currentLatitude, currentLongitude);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        StringBuilder addressText = new StringBuilder("");
        try {
            List<Address> addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                addressText.append(address.getAddressLine(0) + ",\n");
                addressText.append(address.getLocality() + ",\n");
                addressText.append(address.getAdminArea() + ",\n");
                addressText.append(address.getCountryName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.addMarker(new MarkerOptions().position(currentLocation).title(addressText.toString()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}