package com.idn.miniworkshopramadhan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idn.miniworkshopramadhan.R;
import com.idn.miniworkshopramadhan.response.ItemsItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context c;
    List<ItemsItem> dataSholat;

    private String namaSholat[] = {"Shubuh", "Dzuhur", "Ashar", "Magrib", "Isya"};
    private String waktuSholat[];


    public MyAdapter(Context c, List<ItemsItem> dataSholattt) {
        this.c = c;
        dataSholat = dataSholattt;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(c).inflate(R.layout.item_list, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        waktuSholat = new String[]{
                dataSholat.get(getItemCount()).getFajr(),
                dataSholat.get(getItemCount()).getDhuhr(),
                dataSholat.get(getItemCount()).getAsr(),
                dataSholat.get(getItemCount()).getMaghrib(),
                dataSholat.get(getItemCount()).getIsha()};

        holder.tvName.setText(namaSholat[position]);
        holder.tvTimer.setText(waktuSholat[position]);

    }


    @Override
    public int getItemCount() {
        return namaSholat.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_timer)
        TextView tvTimer;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }
}
