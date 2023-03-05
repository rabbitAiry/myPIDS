package com.airy.mypids.pids

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment

abstract class BasePidsFragment: Fragment() {
    /**
     * 获取当前pids名称
     */
    abstract fun getPidsStyleName(): String

    /**
     * 当定位信息靠近站台位置时，此方法就会被调用
     * 而只有车辆已经停下来时，参数[isStopped]才会为true
     */
    abstract fun pidsStationArrived(isStopped: Boolean)

    /**
     * 当车辆重新启动
     */
    abstract fun pidsRunning()

    /**
     * 当车辆离开站台时调用该方法
     */
    abstract fun nextStation()

    final override fun getContext() = activity as Context
    val handler = Handler(Looper.getMainLooper())
    var status = PidsStatus.BUS_STATION_ARRIVED
}