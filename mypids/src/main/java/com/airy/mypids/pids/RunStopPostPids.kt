package com.airy.mypids.pids

import android.content.Context
import com.airy.mypids.data.StationListInfo

/**
 * 该类负责实现pids状态切换的逻辑，ui变化交由其子类负责
 * 该类表示会涉及以下状态的pids：BUS_STATION_ARRIVED, BUS_RUNNING_START, BUS_RUNNING
 */
abstract class RunStopPostStartPids(stations: StationListInfo) : RunStopPids(stations) {
    override fun pidsRunning() {
        if (stations.isLastStation()) return
        if (status == PidsStatus.BUS_STATION_ARRIVED) {
            status = PidsStatus.BUS_RUNNING_START
            val delay = displayRunningStart()
            handler.postDelayed({
                if (status != PidsStatus.BUS_RUNNING_START) return@postDelayed
                pidsRunning()
            }, delay)
        } else {
            status = PidsStatus.BUS_RUNNING
            displayRunning()
        }
    }

    /**
     * 开始行驶时的展示信息。通常会将公告、宣传内容在列车开始行驶时进行展示
     * @return 表示该状态持续时间。该状态结束后，状态改为PidsStatus.BUS_RUNNING
     */
    abstract fun displayRunningStart(): Long
}