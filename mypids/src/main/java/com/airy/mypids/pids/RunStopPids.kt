package com.airy.mypids.pids

import android.content.Context
import com.airy.mypids.data.StationListInfo

/**
 * 该类负责实现pids状态切换的逻辑，ui变化交由其子类负责
 * 该类表示会涉及以下状态的pids：BUS_STATION_ARRIVED, BUS_RUNNING
 */
abstract class RunStopPids(context: Context, val stations: StationListInfo) : BasePidsFragment(context){
    override fun pidsStationArrived(isStopped: Boolean) {
        status = PidsStatus.BUS_STATION_ARRIVED
        displayStationArrived()
    }

    override fun pidsRunning() {
        if (stations.isLastStation()) return
        status = PidsStatus.BUS_RUNNING
        displayRunning()
    }

    override fun nextStation() {
        stations.nextStation()
        displayNextStation()
        if (status == PidsStatus.BUS_STATION_ARRIVED) displayStationArrived()
        else if (status == PidsStatus.BUS_RUNNING) displayRunning()
    }

    /**
     * 展示到站信息
     */
    abstract fun displayStationArrived()

    /**
     * 默认行驶时展示的信息
     */
    abstract fun displayRunning()

    /**
     * 切换至下一站时需要进行的UI更新操作
     */
    abstract fun displayNextStation();
}