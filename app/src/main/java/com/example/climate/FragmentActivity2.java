package com.example.climate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class FragmentActivity2 extends Fragment {

    TextView temperature ;
    EditText cityname;
    TextView citydetails;
    TextView date;
    private Button button2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment2, container, false);

        temperature=(TextView)view.findViewById(R.id.temp);
        cityname=(EditText) view.findViewById(R.id.cityname);
        citydetails=(TextView)view.findViewById(R.id.citydetails);
        date=(TextView)view.findViewById(R.id.date);


        button2 = (Button) view.findViewById(R.id.frag_button2);
        findweather();
        //return super.onCreateView(inflater, container, savedInstanceState);
        return view;
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
        //http://api.openweathermap.org/data/2.5/weather?lat=22.5434679&lon=88.3785973&appid=6dbe05544e37f2e893cdd1ba493d5181
        String url="http://api.openweathermap.org/data/2.5/weather?lat="+lati+"&lon="+longi+"&appid=6dbe05544e37f2e893cdd1ba493d5181";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject=response.getJSONObject("main");
                    JSONArray jsonArray=response.getJSONArray("weather");
                    JSONObject object=jsonArray.getJSONObject(0);
                    String temp=String.valueOf(jsonObject.getDouble("temp"));
                    String description =object.getString("description");
                    String name=response.getString("name");
                    Log.d("Clima",""+name);
                    Log.d("Clima",""+description);
                    Log.d("Clima",""+temp);
                    cityname.setText(name);
                    citydetails.setText(description);
                    temperature.setText(temp);

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
}
