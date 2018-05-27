package com.idn.miniworkshopramadhan.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.idn.miniworkshopramadhan.MainActivity;
import com.idn.miniworkshopramadhan.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (isInternet()) {
            splashScreen();

        } else {
            showAlertDialog(this, "Not Connection",
                    "Turn On Wif or Mobile Data", false);

        }
    }


    private void showAlertDialog(
            Context c, String title,
            String message, Boolean status) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));

            }
        });
        builder.show();

    }


    private void splashScreen() {
        Thread thread = new Thread() {

            public void run() {
                try {
                    sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();

                } finally {
                    Intent i = new Intent(getApplicationContext(),
                            MainActivity.class);

                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(i);
                    finish();

                }
            }
        };
        thread.start();

    }


    public boolean isInternet() {

        ConnectivityManager manager =
                (ConnectivityManager) getBaseContext()
                        .getSystemService(
                                Context.CONNECTIVITY_SERVICE);

        if (manager != null) {
            NetworkInfo[] info = manager.getAllNetworkInfo();

            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;

    }
}
