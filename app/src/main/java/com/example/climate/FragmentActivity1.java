package com.example.climate;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class FragmentActivity1 extends Fragment  implements LocationListener{

    private TextToSpeech textToSpeech;
    private ImageButton button1;

    TextView curlocation;
    TextView mCityLabel;
    TextView web;

    LocationManager locationManager;
    int c=1;
    int d=1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_fragment1,container,false);

        button1=(ImageButton)view.findViewById(R.id.changeCityButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"1",Toast.LENGTH_SHORT).show();
            }
        });
        mCityLabel=(TextView)view.findViewById(R.id.locationTV);
        mCityLabel.setSelected(true);
        web=(TextView)view.findViewById(R.id.tempTV);
        curlocation=(TextView)view.findViewById(R.id.locationTV);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
        //return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(c==1){
            getLocation();
            c++;
        }
    }
    void getLocation() {

        try {
            locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,5, (LocationListener) this);
        }

        catch(SecurityException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(final Location location) {
        try{
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Log.d("climate",""+location.getLatitude());
            Log.d("climate",""+location.getLongitude());
            mCityLabel.setText(addresses.get(0).getAddressLine(0));

            textToSpeech =new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {
                    if(i==TextToSpeech.SUCCESS){
                        int ttsLang=textToSpeech.setLanguage(Locale.US);
                        if(ttsLang==TextToSpeech.LANG_MISSING_DATA || ttsLang==TextToSpeech.LANG_NOT_SUPPORTED){
                            Log.e("Clima","The language is not supported");
                        }
                        else {
                            Log.i("CLima","Language supported");
                            speechfromtext();
                        }
                        Log.i("TTS", "Initialization success.");
                    }

                    else {
                        Toast.makeText(getActivity(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(getActivity(),"Please Enable GPS and Internet", Toast.LENGTH_SHORT);
    }
    public void speechfromtext()
    {

        String data = curlocation.getText().toString();
        //Toast.makeText(getActivity(),""+data,Toast.LENGTH_SHORT).show();
        Log.i("TTS", "button clicked: " + data);
        int speechStatus= textToSpeech.speak(data, TextToSpeech.QUEUE_ADD, null);

        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in converting Text to Speech!");
        }
    }
}
