package com.airy.mypids.pidsStyle;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.airy.mypids.object.Line;
import com.airy.mypids.utils.LineUtil;

enum LineShowStatus{
    BUS_STATION_ARRIVED, BUS_RUNNING_PLAIN, BUS_RUNNING_WITH_NOTICE;
}

public abstract class BasicPidsFragment extends Fragment {
    private static final String TAG = "TestPidsFragment";
    public static final int MESSAGE_SWITCH_LANGUAGE = 1;
    public static final int MESSAGE_STATION_ARRIVED = 2;
    public static final int MESSAGE_BUS_RUNNING_PLAIN = 3;
    public static final int MESSAGE_BUS_RUNNING_WITH_NOTICE = 5;
    protected LineShowStatus status = LineShowStatus.BUS_RUNNING_PLAIN;

    private final Handler handler = new Handler(msg -> {
        switch (msg.what){
            case MESSAGE_SWITCH_LANGUAGE:
                // switch language
                switchLineLanguage();
                return true;
            case MESSAGE_STATION_ARRIVED:
                // 当定位显示到站且车已停下时
                busStationArrived();
                return true;
            case MESSAGE_BUS_RUNNING_PLAIN:
                // 在BUS_RUNNING_WITH_NOTICE状态结束后，不再展示宣传信息
                busRunningPlain();
                return true;
            case MESSAGE_BUS_RUNNING_WITH_NOTICE:
                // 当处于BUS_STATION_ARRIVED状态下，车辆定位刷新时
                // 此时可以在页面中展示宣传一定时间的信息
                // 若无需进行宣传，则该状态会被跳过
                busRunningWithNotice();

        }
        return false;
    });
    public interface ProgressBarSetting {
        void setProgressBarVisibility(int visibility);
    }

    protected Line line;
    private final ProgressBarSetting progressBarSetting;

    public BasicPidsFragment(ProgressBarSetting progressBarSetting) {
        this.progressBarSetting = progressBarSetting;
    }

    /**
     * 用于获取Line对象，并展示progressBar
     * 此方法应该在onCreateView()中被调用，因为涉及到了ui界面
     * 获取成功后，调用inflateData()。可以通过重写此方法来将数据展示到ui中
     * @return
     */
    protected void getLine(Context context){
        progressBarSetting.setProgressBarVisibility(View.VISIBLE);
        new Thread(() -> {
            line = LineUtil.getFakeLineItem(context);
            handler.post(this::inflateData);
        }).start();
    }

    /**
     * 调用此方法时，已经获取到了Line对象
     * 每次初始化该fragment时，都会调用该方法
     * 在每个fragment的生命周期里，该方法只会被调用一次
     */
    protected void inflateData(){
        progressBarSetting.setProgressBarVisibility(View.GONE);
        refreshView();
        switchLineLanguage();
    }

    /**
     * 调用此方法，给handler发送一则“到站了”的消息
     */
    public final void postStationArrived(){
        Message message = new Message();
        message.what = MESSAGE_STATION_ARRIVED;
        handler.sendMessage(message);
    }

    /**
     * 调用此方法，给handler发送一则“车走了”的消息
     */
    public final void postBusRunning(){
        Message message = new Message();
        message.what = MESSAGE_BUS_RUNNING_WITH_NOTICE;
        handler.sendMessage(message);
    }

    /**
     * 在初始化后或者切换语言后被调用
     * 意味着该方法可能会被多次调用
     */
    abstract void refreshView();

    void busStationArrived(){
        line.switchToNextStation();
        status = LineShowStatus.BUS_STATION_ARRIVED;
    }

    void busRunningPlain(){
        status = LineShowStatus.BUS_RUNNING_PLAIN;
    }

    void busRunningWithNotice(){
        status = LineShowStatus.BUS_RUNNING_WITH_NOTICE;
        handler.postDelayed(() -> {
            Message message = new Message();
            message.what = MESSAGE_BUS_RUNNING_PLAIN;
            handler.sendMessage(message);
        }, 10000);
    }

    /**
     * 切换语言
     */
    void switchLineLanguage(){
        handler.postDelayed(() -> {
            line.switchLanguage();
            refreshView();
            Message message = new Message();
            message.what = MESSAGE_SWITCH_LANGUAGE;
            handler.sendMessage(message);
        }, 5000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
}
