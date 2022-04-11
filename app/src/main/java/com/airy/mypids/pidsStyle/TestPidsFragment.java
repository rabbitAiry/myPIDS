package com.airy.mypids.pidsStyle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airy.mypids.PIDSActivity;
import com.airy.mypids.databinding.FragmentTestPidsBinding;

public class TestPidsFragment extends BasicPidsFragment{
    private static final String TAG = "TestPidsFragment";
    private FragmentTestPidsBinding binding;
    private Context context;

    public TestPidsFragment(ProgressBarSetting progressBarSetting) {
        super(progressBarSetting);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTestPidsBinding.inflate(inflater, container, false);
        context = container.getContext();
        getLine(context);
        return binding.getRoot();
    }

    @Override
    protected void inflateData() {
        setText("获取到Line对象");
        super.inflateData();
        binding.buttonNext.setOnClickListener(v->{
            line.switchToNextStation();
            refreshView();
        });
        binding.buttonArrive.setOnClickListener(v->{
            Intent intent = new Intent(PIDSActivity.LineAction);
            intent.putExtra(PIDSActivity.LineAction, PIDSActivity.MESSAGE_STATION_ARRIVED);
            context.sendBroadcast(intent);
        });
        binding.buttonRunning.setOnClickListener(v->{
            Intent intent = new Intent(PIDSActivity.LineAction);
            intent.putExtra(PIDSActivity.LineAction, PIDSActivity.MESSAGE_BUS_RUNNING);
            context.sendBroadcast(intent);
        });
    }

    @Override
    void refreshView() {
        setDesc();
    }

    @Override
    void busRunningWithNotice() {
        super.busRunningWithNotice();
        setText("交通信息尽在掌握！行讯通广州交通微信小程序帮你查询");
    }

    @Override
    void busStationArrived() {
        super.busStationArrived();
        setDesc();
    }

    @Override
    void busRunningPlain() {
        super.busRunningPlain();
        setDesc();
    }

    void setDesc(){
        if(status == LineShowStatus.BUS_STATION_ARRIVED){
            setText(line.getArrivedStationDesc());
        }else{
            setText(line.getNextStationDesc());
        }
    }

    @SuppressLint("SetTextI18n")
    void setText(String text){
        Log.d(TAG, "setText: "+text);
        binding.testText.setText(binding.testText.getText()+text+"\n");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
