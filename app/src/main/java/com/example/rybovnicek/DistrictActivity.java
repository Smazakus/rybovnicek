package com.example.rybovnicek;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class DistrictActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView tvName;
    TextView tvDistrictNumber;
    MapView map;
    Button btnLink;

    District district;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String TAG = "DistrictActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district);

        tvName = (TextView) findViewById(R.id.tvName);
        tvDistrictNumber = (TextView) findViewById(R.id.tvDistrictNumber);
        btnLink = (Button) findViewById(R.id.btnDetail);

        if (getIntent().hasExtra("district")) {
            district = (District) getIntent().getSerializableExtra("district");
            if (district == null) {
                return;
            }

            tvName.setText(district.getName());
            if (district.getRegNumber() != null){
                tvDistrictNumber.setText(String.format(getString(R.string.ev_number), district.getRegNumber()));
            }
        }

        //MAP PART
        Bundle mapViewBundle = null;
        if (savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        map = (MapView) findViewById(R.id.districtMap);
        map.onCreate(mapViewBundle);

        map.getMapAsync(this);

    }

    public void linkToDistrict(View v){

        if (district != null){
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(district.getLink()));
            startActivity(i);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        map.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        map.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        map.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (district.getCoords() == null){
            return;
        }


        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLng tmp = null;
        for (List<Float> coord : district.getCoords()) {
            tmp = new LatLng(coord.get(1), coord.get(0));
            map.addMarker(new MarkerOptions().position(tmp).title("District"));
            builder.include(tmp);
        }

        if (district.getCoords().size() == 1){
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(tmp, 10));
        }
        else {
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = (int) (getResources().getDisplayMetrics().heightPixels * .6);
            int padding = (int) (width * 0.2);
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width, height, padding));
        }

    }

    @Override
    protected void onPause() {
        map.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        map.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }
}
