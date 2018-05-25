package com.idn.miniworkshopramadhan.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


public class MyFunction extends AppCompatActivity {

    public Context c;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        c = MyFunction.this;

    }


    public void intent(Context c, Class destination) {
        startActivity(new Intent(c, destination));

    }


    public void toast(String message) {
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();

    }


    public void toasttt(String message) {
        Toast.makeText(c, message, Toast.LENGTH_LONG).show();

    }


/*    public String currentDate() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat
                = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

        Date date = new Date();
        return dateFormat.format(date);

    }*/
}
