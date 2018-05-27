package com.idn.miniworkshopramadhan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.idn.miniworkshopramadhan.adapter.MyAdapter;
import com.idn.miniworkshopramadhan.helper.MyFunction;
import com.idn.miniworkshopramadhan.network.ConfigRetrofit;
import com.idn.miniworkshopramadhan.response.ResponseApi;
import com.idn.miniworkshopramadhan.ui.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends MyFunction {

    private RecyclerView rv;
    private TextView tvLocation, tvHours, tvDate, tvInputLoc, tvAbout;
    private String loginLocation;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE;

    Status status;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLocation = findViewById(R.id.tv_location);
        tvHours = findViewById(R.id.tv_hours);
        tvDate = findViewById(R.id.tv_date);
        rv = findViewById(R.id.recycler_view);

        //TODO 1 cek login
        LoginActivity.Preference preference =
                new LoginActivity.Preference(MainActivity.this);
        loginLocation = preference.getLocLogin();


        if (TextUtils.isEmpty(loginLocation)) {
            intent(LoginActivity.class);
            finish();

        }


        //TODO 2 buat method
        hours();
        date();
        getScheduleSholat(loginLocation);

    }


    //TODO 5
    private void getScheduleSholat(String detailPlaces) {

        String location = detailPlaces;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat
                = new SimpleDateFormat("yyyy-MM-dd");

        final String currentDateSholat = dateFormat.format(new Date());
        final ProgressDialog dialog = ProgressDialog.show(
                MainActivity.this, "", "Loading", false);

        ConfigRetrofit.service.scheduleSholat(location, currentDateSholat).enqueue(new Callback<ResponseApi>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {

                if (response.isSuccessful()) {
                    dialog.dismiss();

                    rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    MyAdapter adapter = new MyAdapter(MainActivity.this, response.body().getItems());
                    rv.setAdapter(adapter);

                    String kota = response.body().getCity();
                    String province = response.body().getState();
                    String country = response.body().getCountry();

                    tvLocation.setText(kota + ", " + province + ", " + country);

                } else if (response.body().getCity().isEmpty()) {
                    toast("Location Not Found");

                } else {
                    toast("Not Connection");

                }
            }


            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                t.getMessage();

            }
        });
    }


    //TODO 4 sesuaikan berdasarkan date yang ada
    private void date() {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format
                = new SimpleDateFormat("EEE, d MMM yyyy");
        final String currentDate = format.format(new Date());
        tvDate.setText(currentDate);

    }


    //TODO 3 sesuaikan berdasarkan waktu yang ada
    private void hours() {

        final Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void run() {
                tvHours.setText(new SimpleDateFormat("K:mm a").format(new Date()));
                handler.postDelayed(this, 1000);

            }
        }, 10);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_item, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.item_logout:
                logout();
                break;

            case R.id.item_your:
                changeLocation();
                break;

            case R.id.exit:
                System.exit(1);
                break;

        }
        return super.onOptionsItemSelected(item);

    }


    private void changeLocation() {

        LayoutInflater inflater = getLayoutInflater();
        final View alert = inflater.inflate(R.layout.item_location, null);
        tvInputLoc = alert.findViewById(R.id.tv_input_loc);

        tvInputLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNewLoc();

            }
        });

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setView(alert);

        dialog.setPositiveButton("Selesai", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        dialog.show();

    }


    private void findNewLoc() {

        try {

            AutocompleteFilter.Builder filter = new AutocompleteFilter.Builder();
            filter.setCountry("id");

            Intent i = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(filter.build())
                    .build(this);

            startActivityForResult(i, PLACE_AUTOCOMPLETE_REQUEST_CODE);

        } catch (GooglePlayServicesNotAvailableException
                | GooglePlayServicesRepairableException e) {
            e.printStackTrace();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                String placeDetail = place.getName().toString();

                getScheduleSholat(placeDetail);

                tvInputLoc.setText(placeDetail);
                loginLocation = placeDetail + ", Indonesia";

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                status = PlaceAutocomplete.getStatus(this, data);

            } else if (resultCode == RESULT_CANCELED) {
                toast("Canceled");

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void logout() {

        LoginActivity.Preference preference =
                new LoginActivity.Preference(MainActivity.this);
        preference.logout();
        intent(LoginActivity.class);
        finish();

    }
}
