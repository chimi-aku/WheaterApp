package com.example.weatherapp;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<MainData> dataList;
    private Activity context;
    private RoomDB database;

    public MainAdapter(Activity context, List<MainData> dataList) {
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Initialize view
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_main, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Initialize main data
        MainData data = dataList.get(position);
        // Initialize database
        database = RoomDB.getInstance(context);
        // Set text(Location) on textView
        holder.textView.setText(data.getLocation());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize main data
                MainData d = dataList.get(holder.getAdapterPosition());
                // Get ID
                int sId = d.getID();
                // Get text
                String sText = d.getLocation();

                // Create dialog
                Dialog dialog = new Dialog(context);
                // Set Content View
                dialog.setContentView(R.layout.dialog_update);
                // Initialize width
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                // Initialize height
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                // Set layout
                dialog.getWindow().setLayout(width, height);

                dialog.show();

                // Initalize and assign viarable
                EditText editText = dialog.findViewById(R.id.editText);
                Button btnUpdate = dialog.findViewById(R.id.btnUpdate);

                // Set text on edit Text
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss dialog
                        dialog.dismiss();
                        // Get updated location form edit text
                        String uText = editText.getText().toString().trim();
                        // Update location in database
                        database.mainDao().update(sId, uText);
                        //Notify when data is updated
                        dataList.clear();
                        dataList.addAll(database.mainDao().getAll());
                        notifyDataSetChanged();

                    }
                });

            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                // Initialize main data
                MainData d = dataList.get(holder.getAdapterPosition());
                // Delete location form database
                database.mainDao().delete(d);
                // Notify when data is deleted
                int position = holder.getAdapterPosition();
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataList.size());

            }
        });

        holder.btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize main data
                MainData d = dataList.get(holder.getAdapterPosition());
                int sId = d.getID();

                MyProperties.getInstance().chosenLocation = d;
                MyProperties.getInstance().chosenLocationCityName = d.getLocation();


            }
        });



    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView btnEdit, btnDelete, btnChoose;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_view);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnChoose = itemView.findViewById(R.id.btnChoose);
        }
    }

    private void saveChosenToProperties(List<MainData> d) {

        if(d.size() == 1) {
            MainData newChosen = d.get(0);
            MyProperties.getInstance().chosenLocation.setLocation(newChosen.getLocation());
        }

    }
}
