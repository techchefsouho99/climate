package com.example.climate;

import android.app.Activity;


public class ApiDecodeActivity extends Activity {
    public String FahToCenti(float kelvin){
        int centi;
        centi=(int) (kelvin-273);
        return String.valueOf(centi);
    }

}
