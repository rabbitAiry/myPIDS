package com.airy.mypids;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.airy.mypids.databinding.ActivityPidsBinding;
import com.airy.mypids.pidsStyle.BasicPidsFragment;
import com.airy.mypids.pidsStyle.BasicVerticalPidsFragment;
import com.airy.mypids.pidsStyle.TestPidsFragment;

public class PIDSActivity extends AppCompatActivity {
    public static final int MESSAGE_STATION_ARRIVED = 2;
    public static final int MESSAGE_BUS_RUNNING = 3;
    public static final String LineAction = "LINE_ACTION";
    private ActivityPidsBinding binding;
    private BasicPidsFragment fragment;
    private LineActionReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPidsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)actionBar.hide();

        IntentFilter filter = new IntentFilter(LineAction);
        receiver = new LineActionReceiver();
        registerReceiver(receiver, filter);

        FragmentManager manager = getSupportFragmentManager();
        fragment = new TestPidsFragment(new ActivityProgressBarSetting());
        manager.beginTransaction().add(R.id.pids_container, fragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    class LineActionReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra(LineAction) && fragment!=null){
                int message = intent.getIntExtra(LineAction, 0);
                switch (message){
                    case MESSAGE_STATION_ARRIVED:
                        fragment.postStationArrived();
                        break;
                    case MESSAGE_BUS_RUNNING:
                        fragment.postBusRunning();
                }
            }
        }
    }

    class ActivityProgressBarSetting implements BasicPidsFragment.ProgressBarSetting {
        @Override
        public void setProgressBarVisibility(int visibility) {
            binding.pidsProgressBar.setVisibility(visibility);
        }
    }

}