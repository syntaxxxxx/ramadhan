package com.idn.miniworkshopramadhan.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.idn.miniworkshopramadhan.MainActivity;
import com.idn.miniworkshopramadhan.R;
import com.idn.miniworkshopramadhan.helper.MyFunction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginPreference extends MyFunction {

    @BindView(R.id.tv_login_loc)
    TextView tvLoginLoc;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    public Preference preference;
    public static String name = null;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_preference);
        ButterKnife.bind(this);

        preference = new Preference(LoginPreference.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 2);

        }
    }


    @OnClick({R.id.tv_login_loc, R.id.btn_submit})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.tv_login_loc:
                findLocation();
                break;

            case R.id.btn_submit:
                Submit();
                break;

        }
    }


    private void findLocation() {

        try {
            AutocompleteFilter.Builder filter =
                    new AutocompleteFilter.Builder();

            filter.setCountry("id");
            Intent i = new PlaceAutocomplete.IntentBuilder(
                    PlaceAutocomplete.MODE_OVERLAY).setFilter(
                    filter.build()).build(this);

            startActivityForResult(i, PLACE_AUTOCOMPLETE_REQUEST_CODE);

        } catch (GooglePlayServicesNotAvailableException
                | GooglePlayServicesRepairableException e) {

            e.printStackTrace();

        }
    }


    public void Submit() {

        name = tvLoginLoc.getText().toString().trim();

        if (name.isEmpty()) {
            toasttt("You Must Input Your Location");

        } else {
            preference.setLocLogin(name);
            intent(this, MainActivity.class);
            finish();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                tvLoginLoc.setText(place.getName());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                status.getStatus();

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }


    public static class Preference {

        String KEY_NAME = "name";
        String KEY_SAVE = "save";

        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;

        public Preference(Context c) {
            sharedPreferences = c.getSharedPreferences(
                    KEY_SAVE, Context.MODE_PRIVATE);
        }


        public String getLocLogin() {
            return sharedPreferences.getString(KEY_NAME,
                    null);
        }


        public void setLocLogin(String name) {
            editor = sharedPreferences.edit();
            editor.putString(KEY_NAME, name).apply();
        }


        public void logout() {
            editor = sharedPreferences.edit();
            editor.clear().apply();
        }
    }
}
