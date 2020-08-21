package com.example.rybovnicek;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FishAdapter extends RecyclerView.Adapter<FishAdapter.ViewHolder> {

    private Context context;
    private String district;
    private ArrayList<Fish> fishList;

    public FishAdapter(Context context, String district, ArrayList<Fish> fishList) {
        this.context = context;
        this.fishList = fishList;
        this.district = district;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fish, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Fish fish = fishList.get(position);

        holder.tvName.setText(fish.getName());
        holder.tvLength.setText(String.format(context.getString(R.string.cmLength), fish.getLength()));

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(fish.getDate() * 1000);
        holder.tvDate.setText(DateFormat.format("dd. MM. yyyy", cal).toString());

    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        if (fishList != null) {
            return fishList.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public final View view;
        public final TextView tvDate;
        public final TextView tvName;
        public final TextView tvLength;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            tvDate = view.findViewById(R.id.tvDate);
            tvName = view.findViewById(R.id.tvName);
            tvLength = view.findViewById(R.id.tvLength);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle(R.string.fish_menu_title);
            MenuItem edit = menu.add(Menu.NONE, 1, 1, R.string.fish_update);
            MenuItem delete = menu.add(Menu.NONE, 2, 2, R.string.fish_remove);

            edit.setOnMenuItemClickListener(onEditMenu);
            delete.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                final int position = getAdapterPosition();
                final Fish clickedFish = fishList.get(position);

                switch (item.getItemId()) {
                    case 1:
                        Intent i = new Intent(context, FishFormActivity.class);
                        i.putExtra("fish", clickedFish);
                        i.putExtra("districtName", district);
                        ((Activity) context).startActivityForResult(i, 0);
                        break;

                    case 2:
                        AlertDialog builder = new AlertDialog.Builder(context)
                                .setTitle(R.string.remove_fish_title)
                                .setMessage(String.format(context.getString(R.string.romve_fish_text), clickedFish.getName(), clickedFish.getLength()))
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        removeFish(clickedFish);
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();

                        builder.create();
                        break;
                }
                return true;
            }

            private void removeFish(final Fish clickedFish){
                String url = "https://sycakovo.krumpac.net/findDistrict/fish/remove";

                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("SUCCESS")){
                                    Toast.makeText(context, "Úlovek smazán", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(context, "Úlovek se nepodařilo smazat", Toast.LENGTH_LONG).show();
                                }
                                fishList.remove(clickedFish);
                                notifyDataSetChanged();
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
                        params.put("fishId", String.valueOf(clickedFish.getId()));
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(postRequest);
            }
        };


    }
}
