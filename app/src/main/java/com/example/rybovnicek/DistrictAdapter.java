package com.example.rybovnicek;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DistrictAdapter extends RecyclerView.Adapter {

    private ArrayList<District> districts;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_district, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        District district = districts.get(position);

        ((ViewHolder) holder).name.setText(district.getName());
        ((ViewHolder) holder).distance.setText(String.format("%d km", district.getDistance()));
    }

    @Override
    public int getItemCount() {
        if (districts != null) {
            return districts.size();
        } else {
            return 0;
        }
    }

    public DistrictAdapter(ArrayList<District> districts) {
        this.districts = districts;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView name;
        public final TextView distance;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.tvDistrictName);
            distance = view.findViewById(R.id.tvDistance);
        }

    }
}
