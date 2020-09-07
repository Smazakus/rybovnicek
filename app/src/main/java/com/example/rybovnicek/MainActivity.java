package com.example.rybovnicek;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;
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

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, LocationListener {

    private static final int REQUEST_LOCATION = 1;
    TextView tvEmptyDistrictList;
    Button btnDistricts;
    ProgressBar progress;
    LocationManager locationManager;
    Location lastLocation;
    Toolbar mainToolbar;

    SharedPreferences prefs;

    private RecyclerView districts;
    private RecyclerView.Adapter adapter;
    private ArrayList<District> districtList;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);

        btnDistricts = findViewById(R.id.btnDistricts);
        progress = findViewById(R.id.pbLoad);

        this.districts = findViewById(R.id.listDistricts);
        districts.addItemDecoration(new DividerItemDecoration(districts.getContext(), DividerItemDecoration.VERTICAL));

        tvEmptyDistrictList = findViewById(R.id.tvEmptyDistrictList);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        districtList = new ArrayList<District>();
        adapter = new DistrictAdapter(this, districtList);
        this.districts.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        this.districts.setLayoutManager(mLayoutManager);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        listenPosition();
    }

    private void getDistrictsByLocation(Location location) {

        progress.setVisibility(View.VISIBLE);

        boolean trout = prefs.getBoolean("mrs_trout", false);
        boolean notTrout = prefs.getBoolean("mrs_not_trout", false);

        if (!trout && !notTrout){
            tvEmptyDistrictList.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            districtList.clear();
            adapter.notifyDataSetChanged();
            return;
        }
        else {
            tvEmptyDistrictList.setVisibility(View.INVISIBLE);
        }

        String url = "https://sycakovo.krumpac.net/findDistrict/?lat=" + String.valueOf(location.getLatitude()) + "&lon=" + String.valueOf(location.getLongitude());
        if (trout){
            url += "&trout=true";
        }
        if (notTrout) {
            url += "&ntrout=true";
        }

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type listDistrictType = new TypeToken<List<District>>() { }.getType();
                ArrayList<District> list = gson.fromJson(response, listDistrictType);
                districtList.clear();
                districtList.addAll(list);
                adapter.notifyDataSetChanged();
                progress.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void getLocClick(View v) {
        Location loc = null;
        loc = getLocation();

        Log.d("POSITION", String.valueOf(loc));

        if (loc != null) {
            getDistrictsByLocation(loc);
        }

    }

    private void listenPosition(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        }

        if (ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Nullable
    private Location getLocation() {
        if (! locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
            return null;
        } else {
            if (lastLocation != null) {
                return lastLocation;
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Location loc;
        loc = getLocation();
        if (loc != null){
            getDistrictsByLocation(loc);
        }
    }

    @Override
    public void onLocationChanged(@NotNull Location location) {
        lastLocation = location;
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

}