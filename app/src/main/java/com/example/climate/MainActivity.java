package com.example.climate;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";

    // App ID to use OpenWeather data
    final String APP_ID = "e72ca729af228beabd5d20e3b7749713";

    // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 5000;

    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1000;

    // Set LOCATION_PROVIDER here. Using GPS_Provider for Fine Location (good for emulator):
    // Recommend using LocationManager.NETWORK_PROVIDER on physical devices (reliable & fast!)
    final String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;

    final int REQUEST_CODE=123;

    final String LOGCAT_TAG = "Clima";

    // Member Variables:

    boolean mUseLocation=true;
    TextView mCityLabel;
    ImageView mWeatherImage;
    TextView mTemperatureLabel;

    // Declaring a LocationManager and a LocationListener here:
    LocationManager mLocationManager;
    LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    // onResume() life cyle callback:

    @Override
    protected void onResume() {
        super.onResume();
        if(mUseLocation) getWeatherForCurrentLocation();
        Log.d("clima", "onresume called()");

    }

    private void getWeatherForCurrentLocation() {
        Log.d("clima","hello");
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("clima", "on location changed");

                String longitude=String.valueOf(location.getLongitude());
                String latitute=String.valueOf(location.getLatitude());
                Log.d("clima","latitude"+latitute);
                Log.d("clima","longitude"+longitude);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Log.d("clima", "on provider disabled");
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_CODE);
            return;
        }

        // Some additional log statements to help you debug
        Log.d(LOGCAT_TAG, "Location Provider used: "
                + mLocationManager.getProvider(LOCATION_PROVIDER).getName());
        Log.d(LOGCAT_TAG, "Location Provider is enabled: "
                + mLocationManager.isProviderEnabled(LOCATION_PROVIDER));
        Log.d(LOGCAT_TAG, "Last known location (if any): "
                + mLocationManager.getLastKnownLocation(LOCATION_PROVIDER));
        Log.d(LOGCAT_TAG, "Requesting location updates");
        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);

    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==REQUEST_CODE){
            if (grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getWeatherForCurrentLocation();
                Log.d("clima","log granted");
            }else {
                Log.d("clima","log denied");
            }
        }
    }*/
}
