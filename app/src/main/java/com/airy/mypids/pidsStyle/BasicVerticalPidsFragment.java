package com.airy.mypids.pidsStyle;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.airy.mypids.R;
import com.airy.mypids.adapter.BasicStationAdapter;
import com.airy.mypids.object.Line;
import com.airy.mypids.data.StationDataRepo;
import com.airy.mypids.databinding.FragmentBasicVerticalPidsBinding;

public class BasicVerticalPidsFragment extends Fragment {
    public static final int MESSAGE_SWITCH_LANGUAGE = 1;
    public interface ProgressBarSetting {
        void setProgressBarVisibility(int visibility);
    }

    private final ProgressBarSetting progressBarSetting;
    private FragmentBasicVerticalPidsBinding binding;
    private final Handler handler = new Handler(msg -> {
        if(msg.what == MESSAGE_SWITCH_LANGUAGE){
            switchLineLanguage();
            return true;
        }
        return false;
    });

    private StationDataRepo repo;
    private BasicStationAdapter adapter;
    private Line line;
    private Context context;

    public BasicVerticalPidsFragment(ProgressBarSetting progressBarSetting) {
        this.progressBarSetting = progressBarSetting;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBasicVerticalPidsBinding.inflate(inflater, container, false);
        repo = StationDataRepo.getInstance();
        context = getContext();
        inflateLineData();
        return binding.getRoot();
    }

    private void inflateLineData() {
        progressBarSetting.setProgressBarVisibility(View.VISIBLE);
        new Thread(() -> {
            line = repo.getFakeLineItem(context);
            handler.post(() -> {
                progressBarSetting.setProgressBarVisibility(View.GONE);
                setText();
                adapter = new BasicStationAdapter(line);
                binding.lineStationList.setAdapter(adapter);
                binding.lineStationList.setLayoutManager(new LinearLayoutManager(getContext()));
                switchLineLanguage();
            });
        }).start();
    }

    private void setText(){
        // error: 当退出pids页面后，handler仍在继续工作，导致了数据更新在null上
//        binding.lineName.setEllipsize();
        binding.lineName.setText(line.lineName);
        binding.lineNextTag.setText(line.getTextOfNextStation());
        binding.lineTermination.setText(line.getTerminalDesc());
        binding.lineNextStation.setText(line.getStationName(1));
    }

    public void switchLineLanguage() {
        handler.postDelayed(() -> {
            line.switchLanguage();
            setText();
            adapter.updateView();
            Message message = new Message();
            message.what = MESSAGE_SWITCH_LANGUAGE;
            handler.sendMessage(message);
        }, 5000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
        binding = null;
    }
}
