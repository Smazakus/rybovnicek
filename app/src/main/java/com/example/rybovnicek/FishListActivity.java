package com.example.rybovnicek;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
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

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FishListActivity extends AppCompatActivity {

    TextView tvName, tvLength, tvDate, tvEmptyList, tvTitle;
    RecyclerView rvFishList;
    ProgressBar progress;

    private int districtNumber;
    private String districtName;

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

        if (getIntent().hasExtra("districtName")){
            districtName = getIntent().getStringExtra("districtName");
        }
        else {
            districtName = "";
        }

        tvName = findViewById(R.id.tvName);
        tvLength = findViewById(R.id.tvLength);
        tvDate = findViewById(R.id.tvDate);
        tvEmptyList = findViewById(R.id.tvEmptyList);
        tvTitle = findViewById(R.id.tvFishListTitle);
        tvTitle.setText(String.format(getString(R.string.fish_list_title), districtName));
        progress = (ProgressBar) findViewById(R.id.pbLoad);
        rvFishList = findViewById(R.id.rvFishList);
        rvFishList.addItemDecoration(new DividerItemDecoration(rvFishList.getContext(), DividerItemDecoration.VERTICAL));

        fishList = new ArrayList<Fish>();
        adapter = new FishAdapter(this, districtName, fishList);
        rvFishList.setAdapter(adapter);
        rvFishList.setLayoutManager(new LinearLayoutManager(this));

        getFishByDistrict(districtNumber);
    }

    private void getFishByDistrict(int district){
        progress.setVisibility(View.VISIBLE);
        String url = "https://sycakovo.krumpac.net/findDistrict/fish?district=" + String.valueOf(district);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type listFishType = new TypeToken<List<Fish>>() { }.getType();
                ArrayList<Fish> tmpList = gson.fromJson(response, listFishType);
                fishList.clear();
                fishList.addAll(tmpList);
                adapter.notifyDataSetChanged();
                progress.setVisibility(View.GONE);

                if (fishList.size() == 0){
                    tvEmptyList.setVisibility(View.VISIBLE);
                }
                else {
                    tvEmptyList.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getFishByDistrict(districtNumber);
    }
}
