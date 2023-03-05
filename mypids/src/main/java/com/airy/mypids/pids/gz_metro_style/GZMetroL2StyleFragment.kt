package com.airy.mypids.pids.gz_metro_style

import android.content.Context
import com.airy.mypids.pids.BasePidsFragment

class GZMetroL2StyleFragment(context: Context): BasePidsFragment() {
    override fun getPidsStyleName(): String = "广州地铁二号线风格"
    override fun pidsStationArrived(isStopped: Boolean) {
        TODO("Not yet implemented")
    }

    override fun pidsRunning() {
        TODO("Not yet implemented")
    }

    override fun nextStation() {
        TODO("Not yet implemented")
    }
}