package com.example.climate;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class FragmentActivity1 extends Fragment  implements LocationListener{

    private TextToSpeech textToSpeech;

    TextView curcity;
    TextView curlocation;
    TextView current_location_address;
    TextView current_temperature;

    LocationManager locationManager;
    int c=1;
    int d=1;
    public String lati;
    public String longi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_fragment1,container,false);


        current_location_address =(TextView)view.findViewById(R.id.current_location);
        current_location_address.setSelected(true);
        current_temperature=(TextView)view.findViewById(R.id.current_temperature);
        curcity=(TextView)view.findViewById(R.id.current_city);
        curlocation=(TextView)view.findViewById(R.id.current_location);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
        //return super.onCreateView(inflater, container, savedInstanceState);
        findweather();
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
            current_location_address.setText(addresses.get(0).getAddressLine(0));

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
    public void findweather(){
        Log.d("CLima","findweather");
        SharedPreferences sharedPreferences=getActivity().getPreferences(Context.MODE_PRIVATE);
        float lat= (float) sharedPreferences.getFloat("lat", 2);
        String lati=String.valueOf(lat);
        float lon= (float) sharedPreferences.getFloat("long",2);
        String longi=String.valueOf(lon);
        Log.d("Clima",""+lati);
        Log.d("Clima",""+longi);
        String url="http://api.openweathermap.org/data/2.5/weather?lat="+lati+"&lon="+longi+"&appid=6dbe05544e37f2e893cdd1ba493d5181";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject=response.getJSONObject("main");
                    JSONArray jsonArray=response.getJSONArray("weather");
                    JSONObject object=jsonArray.getJSONObject(0);
                    float temp=Float.valueOf((float) jsonObject.getDouble("temp"));
                    String description =object.getString("description");
                    String name=response.getString("name");
                    Log.d("Clima",""+name);
                    Log.d("Clima",""+description);
                    Log.d("Clima",""+temp);
                    curcity.setText(name);
                    ApiDecodeActivity apiDecodeActivity=new ApiDecodeActivity();

                    current_temperature.setText(apiDecodeActivity.FahToCenti(temp)+(char)0x00B0+"C");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue= (RequestQueue) Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(jsonObjectRequest);
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
