package com.airy.mypidshelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.airy.mypidshelper.databinding.ActivityHelperHomeBinding;

enum LineShowStatus{
    BUS_STATION_ARRIVED(false, false, true),
    BUS_RUNNING(true, true, false);
    public boolean buttonNextStationEnable;
    public boolean buttonArriveEnable;
    public boolean buttonRunAfterArriveEnable;

    LineShowStatus(boolean buttonNextStationEnable, boolean buttonArriveEnable, boolean buttonRunAfterArriveEnable) {
        this.buttonNextStationEnable = buttonNextStationEnable;
        this.buttonArriveEnable = buttonArriveEnable;
        this.buttonRunAfterArriveEnable = buttonRunAfterArriveEnable;
    }
}

public class HelperHomeActivity extends AppCompatActivity {
    private ActivityHelperHomeBinding binding;
    private LineShowStatus status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHelperHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        status = LineShowStatus.BUS_RUNNING;
        setButtonStatus();
        binding.helperButtonNextStation.setOnClickListener(v -> {
            // ...
        });
        binding.helperButtonArrive.setOnClickListener(v -> {
            // ...
            status = LineShowStatus.BUS_STATION_ARRIVED;
        });
        binding.helperButtonRunAfterArrive.setOnClickListener(v -> {
            // ...
            status = LineShowStatus.BUS_RUNNING;
        });
    }

    private void setButtonStatus() {
        binding.helperButtonNextStation.setEnabled(status.buttonNextStationEnable);
        binding.helperButtonArrive.setEnabled(status.buttonArriveEnable);
        binding.helperButtonRunAfterArrive.setEnabled(status.buttonRunAfterArriveEnable);
    }
}