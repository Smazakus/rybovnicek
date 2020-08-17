package com.example.rybovnicek;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class DistrictAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<District> districts;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_district, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        District district = districts.get(position);

        ((ViewHolder) holder).name.setText(district.getName());
        ((ViewHolder) holder).distance.setText(String.format("%.2f km", district.getDistance()));

        ((ViewHolder) holder).layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DistrictActivity.class);
                intent.putExtra("district", districts.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (districts != null) {
            return districts.size();
        } else {
            return 0;
        }
    }

    public DistrictAdapter(Context context, ArrayList<District> districts) {
        this.context = context;
        this.districts = districts;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView name;
        public final TextView distance;
        public final ConstraintLayout layout;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.tvDistrictName);
            distance = view.findViewById(R.id.tvDistance);
            layout = view.findViewById(R.id.listItemLayout);
        }

    }
}
