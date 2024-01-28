package com.example.myetsiit.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;


import com.example.myetsiit.R;

import java.util.List;

public class LocationMenuAdapter extends RecyclerView.Adapter<LocationMenuAdapter.LocationViewHolder> {

    private List<String> locationList;
    private OnLocationClickListener clickListener;

    private Context context;

    public LocationMenuAdapter(List<String> locationList, Context context, OnLocationClickListener clickListener) {
        this.locationList = locationList;
        this.context = context; // Set the context
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_menu_item, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        String location = locationList.get(position);
        holder.bind(location);
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    // Importante: aquí tuve que cambiar cosas porque la parte roja del boton no era clickeable
    public class LocationViewHolder extends RecyclerView.ViewHolder {

        private Button locationButton;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationButton = itemView.findViewById(R.id.locationButton);

            locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onLocationClick(locationList.get(position));
                    }
                }
            });
        }

        public void bind(String location) {
            locationButton.setText(location);
        }
    }


    public interface OnLocationClickListener {
        void onLocationClick(String location);
    }
}
