package com.example.rybovnicek;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FishAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Fish> fishList;

    public FishAdapter(Context context, ArrayList<Fish> fishList) {
        this.context = context;
        this.fishList = fishList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fish, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Fish fish = fishList.get(position);

        ((ViewHolder) holder).tvName.setText(fish.getName());
        ((ViewHolder) holder).tvLength.setText(fish.getLength() + " cm");

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(fish.getDate() * 1000);
        ((ViewHolder) holder).tvDate.setText(DateFormat.format("dd. MM. yyyy", cal).toString());
    }

    @Override
    public int getItemCount() {
        if (fishList != null) {
            return fishList.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView tvDate;
        public final TextView tvName;
        public final TextView tvLength;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            tvDate = view.findViewById(R.id.tvDate);
            tvName = view.findViewById(R.id.tvName);;
            tvLength = view.findViewById(R.id.tvLength);;
        }


    }
}
