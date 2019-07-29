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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class FragmentActivity3 extends Fragment {

    private Button button3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment3, container, false);
        Log.d("Clima","hello");
        /*button3 = (Button) view.findViewById(R.id.frag_button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();
            }
        });*/
        //return super.onCreateView(inflater, container, savedInstanceState);
        SharedPreferences sharedPreferences=getActivity().getPreferences(Context.MODE_PRIVATE);
        float lat= (float) sharedPreferences.getFloat("lat", 2);
        String lati=String.valueOf(lat);
        float lon= (float) sharedPreferences.getFloat("long",2);
        String longi=String.valueOf(lon);
        Log.d("Clima",""+lati);
        Log.d("Clima",""+longi);

        WebView webView=(WebView)view.findViewById(R.id.webview);
        webView.loadUrl("https://www.windy.com/-Temperature-temp?temp,18.857,86.561,7");
        return view;
    }

}
