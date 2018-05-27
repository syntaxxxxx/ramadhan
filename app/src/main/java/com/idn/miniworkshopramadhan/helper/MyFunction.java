package com.idn.miniworkshopramadhan.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by idn on 5/27/2018.
 */

public class MyFunction extends AppCompatActivity {

    private Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = MyFunction.this;

    }

    public void intent(Class destination) {
        startActivity(new Intent(c, destination));

    }

    public void toast(String message) {
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();

    }

    public void toasttt(String message) {
        Toast.makeText(c, message, Toast.LENGTH_LONG).show();

    }
}
