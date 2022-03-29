package com.airy.mypids.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airy.mypids.R;

public class BasicStationHolder extends RecyclerView.ViewHolder {
    View stationStatus;
    TextView stationName;

    public BasicStationHolder(@NonNull View itemView) {
        super(itemView);
        stationStatus = itemView.findViewById(R.id.item_station_status);
        stationName = itemView.findViewById(R.id.item_station_name);
    }
}
