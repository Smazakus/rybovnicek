package com.example.rybovnicek;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FishListActivity extends AppCompatActivity {

    TextView tvName, tvLength, tvDate;
    RecyclerView rvFishList;

    private int districtNumber;

    private RecyclerView.Adapter adapter;
    private ArrayList<Fish> fishList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish_list);

        if (getIntent().hasExtra("districtNumber")) {
            districtNumber = (int) getIntent().getIntExtra("districtNumber", 0);
        }
        else {
            districtNumber = 0;
        }

        tvName = (TextView) findViewById(R.id.tvName);
        tvLength = (TextView) findViewById(R.id.tvLength);
        tvDate = (TextView) findViewById(R.id.tvDate);
        rvFishList = (RecyclerView) findViewById(R.id.rvFishList);
        rvFishList.addItemDecoration(new DividerItemDecoration(rvFishList.getContext(), DividerItemDecoration.VERTICAL));

        fishList = new ArrayList<Fish>();
        adapter = new FishAdapter(this, fishList);
        rvFishList.setAdapter(adapter);
        rvFishList.setLayoutManager(new LinearLayoutManager(this));

        getFishByDistrict(districtNumber);
    }

    private void getFishByDistrict(int district){
        String url = "https://sycakovo.krumpac.net/findDistrict/fish?district=" + String.valueOf(district);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type listFishType = new TypeToken<List<Fish>>() { }.getType();
                ArrayList<Fish> tmpList = gson.fromJson(response, listFishType);
                fishList.addAll(tmpList);
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
