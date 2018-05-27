package com.idn.miniworkshopramadhan.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.idn.miniworkshopramadhan.MainActivity;
import com.idn.miniworkshopramadhan.R;

public class LoginActivity extends AppCompatActivity {

    private Button btnSubmit;
    Preference preference;
    private TextView tvLoginLocAwal;

    private String name = null;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    Status status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preference = new Preference(LoginActivity.this);

        btnSubmit = findViewById(R.id.btn_submit);
        tvLoginLocAwal = findViewById(R.id.tv_login_loc);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

            tvLoginLocAwal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findLocation();

                }
            });


            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitLocation();

                }
            });
        }
    }


    private void submitLocation() {

        name = tvLoginLocAwal.getText().toString();

        //Todo cek apabila kosong
        if (name.isEmpty()) {
            Toast.makeText(LoginActivity.this,
                    "Masukkan lokasi terlebih dahulu",
                    Toast.LENGTH_SHORT).show();

        } else {
            preference.setLocLogin(name);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();

        }
    }


    private void findLocation() {

        try {

            AutocompleteFilter.Builder filter = new AutocompleteFilter.Builder();
            filter.setCountry("id");

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(filter.build())
                    .build(this);

            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

        } catch (GooglePlayServicesRepairableException
                | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(this, data);
                tvLoginLocAwal.setText(place.getName());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                status = PlaceAutocomplete.getStatus(this, data);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public static class Preference {

        String KEY_NAME = "NAMA";
        String PREF_NAME = "SIMPAN";
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;

        public Preference(Context c) {
            sharedPreferences = c.getSharedPreferences(PREF_NAME,
                    Context.MODE_PRIVATE);
        }


        public String getLocLogin() {
            return sharedPreferences.getString(KEY_NAME, null);
        }


        public void setLocLogin(String nama) {
            editor = sharedPreferences.edit();
            editor.putString(KEY_NAME, nama).apply();
        }


        public void logout() {
            editor = sharedPreferences.edit();
            editor.clear().apply();
        }
    }
}
