package com.idn.miniworkshopramadhan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.idn.miniworkshopramadhan.ui.LoginPreference;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends MyFunction {

    @BindView(R.id.iv_masjid)
    ImageView ivMasjid;
    @BindView(R.id.txt_date)
    TextView txtDate;
    @BindView(R.id.tv_hours)
    TextView tvHours;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private TextView tvInputanLocation, tvAbout;

    String loginLocation;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE;

    Status status;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //TODO 1 cek login
        LoginPreference.Preference p =
                new LoginPreference.Preference(MainActivity.this);
        loginLocation = p.getLocLogin();

        //TODO 2 kalo kosong harus login dulu
        if (TextUtils.isEmpty(loginLocation)) {
            intent(this, LoginPreference.class);
            finish();

            hours();
            date();
            getScheduleSholat(loginLocation);

        }
    }


    //TODO 3 menampilkan jam saat ini
    private void hours() {

        final Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void run() {

                tvHours.setText(new SimpleDateFormat(
                        "K:mm a").format(new Date()));
                handler.postDelayed(this, 1000);

            }
        }, 10);
    }


    //TODO 4 menampilkan tanggal dan hari saat ini
    private void date() {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat
                = new SimpleDateFormat("EEE, d MMM yyyy");
        final String currentDate = dateFormat.format(new Date());

        txtDate.setText(currentDate);

    }


    //TODO 5 get schedule sholat
    public void getScheduleSholat(String placeDetail) {

        String location = placeDetail;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd");

        final String currentDate = dateFormat.format(new Date());

        final ProgressDialog dialog = ProgressDialog.show(
                MainActivity.this, "Waiting",
                ". . . . . . . . . .",
                false);


        ConfigRetrofit.service.scheduleSholat(location, currentDate)
                .enqueue(new Callback<ResponseApi>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<ResponseApi> call, @NonNull Response<ResponseApi> response) {

                        if (response.isSuccessful()) {
                            dialog.dismiss();

                            recyclerView.setLayoutManager(new LinearLayoutManager(
                                    MainActivity.this));
                            MyAdapter adapter = new MyAdapter(MainActivity.this,
                                    response.body().getItems());
                            recyclerView.setAdapter(adapter);

                            String kota = response.body().getCity();
                            String provinsi = response.body().getState();
                            String country = response.body().getCountry();

                            tvLocation.setText(kota + ", " + provinsi + ", " + country);

                        } else if (response.body().getAddress().isEmpty()) {
                            toast("Location Not Found");

                        } else {
                            toast("Not Internet");

                        }
                    }


                    @Override
                    public void onFailure(@NonNull Call<ResponseApi> call, @NonNull Throwable t) {
                        t.getMessage();
                        toasttt("Something Wrong");

                    }
                });
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
                dialogLogout();
                break;

            case R.id.item_your:
                dialogLocation();
                break;

            case R.id.about:
                break;

        }

        return super.onOptionsItemSelected(item);

    }


    private void dialogLogout() {

        LoginPreference.Preference p =
                new LoginPreference.Preference(MainActivity.this);
        p.logout();
        intent(this, LoginPreference.class);
        finish();

    }


    private void dialogLocation() {

        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View alertLayout = inflater.inflate(
                R.layout.item_location, null);

        tvInputanLocation = alertLayout.findViewById(R.id.tv_input_loc);

        tvInputanLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNewLocation();

            }
        });


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        alert.setCancelable(false);

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alert.show();

    }


    private void findNewLocation() {

        try {
            AutocompleteFilter.Builder filter = new AutocompleteFilter.Builder();
            filter.setCountry("id");

            Intent intent = new PlaceAutocomplete.IntentBuilder(
                    PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(filter.build())
                    .build(this);

            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

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

                String detailPlace = place.getName().toString();
                tvInputanLocation.setText(detailPlace);
                getScheduleSholat(detailPlace);
                loginLocation = detailPlace + ", Indonesia";

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                status = PlaceAutocomplete.getStatus(this, data);

            } else if (resultCode == RESULT_CANCELED) {
                toast("Canceled");

            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}
