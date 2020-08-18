package com.example.rybovnicek;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FishFormActivity extends AppCompatActivity {

    DatePickerDialog picker;

    TextView tvFormDistrict;
    EditText eDate, eType, eLength;

    private int districtNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish_form);

        tvFormDistrict = (TextView) findViewById(R.id.tvFormDistrct);
        eDate = (EditText) findViewById(R.id.eDate);
        eType = (EditText) findViewById(R.id.eFishType);
        eLength = (EditText) findViewById(R.id.eFishLength);

        districtNumber = (int) getIntent().getIntExtra("districtNumber", 0);
        if (getIntent().hasExtra("districtName")) {
            String dName = (String) getIntent().getStringExtra("districtName");
            tvFormDistrict.setText(String.format(getString(R.string.form_districtName), dName));
        }

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        eDate.setText(day + ". " + (month + 1) + ". " + year);
        eDate.setInputType(InputType.TYPE_NULL);
        eDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                picker = new DatePickerDialog(FishFormActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eDate.setText(dayOfMonth + ". " + (monthOfYear + 1) + ". " + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

    }

    public void addFish(View v){
        String url = "https://sycakovo.krumpac.net/findDistrict/fish/add";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        finish();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                DateFormat formatter = new SimpleDateFormat("dd. MM. yyyy");
                Date date = null;
                try {
                    Log.i("CAS", String.valueOf(eDate.getText()));
                    date = (Date) formatter.parse(String.valueOf(eDate.getText()));
                    long timestamp = (long) date.getTime() / 1000;
                    Log.i("CAS", String.valueOf(timestamp));
                    params.put("date", String.valueOf(timestamp));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                params.put("district", String.valueOf(districtNumber));
                params.put("name", String.valueOf(eType.getText()));
                params.put("length", String.valueOf(eLength.getText()));

                Log.d("PRIDAT", "Odeslano");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(postRequest);
    }

}
