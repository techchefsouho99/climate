package com.example.climate;

import android.app.Activity;


public class ApiDecodeActivity extends Activity {

    public String KevToCenti(float kelvin){
        int centi;
        centi=(int) (kelvin-273);
        return String.valueOf(centi);
    }

    public String ImageUpgrade(String details){
        /*if (details=="few clouds") {

            current_weather_image.setImageResource(R.drawable.few_clouds);
        }
        if (details=="haze"){
            current_weather_image.setImageResource(R.drawable.haze);
        }*/
        return "haze";

    }
}
