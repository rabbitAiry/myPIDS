package com.airy.mypids.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airy.mypids.R;
import com.airy.mypids.object.Line;
import com.airy.mypids.object.Station;

import java.util.List;

public class BasicStationAdapter extends RecyclerView.Adapter<BasicStationHolder> {
    private final Line line;

    public BasicStationAdapter(Line line) {
        this.line = line;
    }

    @NonNull
    @Override
    public BasicStationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BasicStationHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_station_basic, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BasicStationHolder holder, int position) {
        int adapterPosition = holder.getAdapterPosition();
        holder.stationName.setText(line.getStationName(adapterPosition));
    }

    @Override
    public int getItemCount() {
        return line==null?0:line.stationItemList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateView(){
        notifyDataSetChanged();
    }
}
