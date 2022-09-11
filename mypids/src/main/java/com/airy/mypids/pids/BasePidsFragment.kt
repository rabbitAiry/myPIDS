package com.airy.mypids.pids

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment

abstract class BasePidsFragment(private val context: Context) : Fragment() {
    abstract fun getPidsStyleName(): String
    abstract fun pidsStationArrived()
    abstract fun pidsRunning()
    abstract fun pidsRunningArriveSoon()
    abstract fun nextStation()

    val handler = Handler(Looper.getMainLooper())
    var status = PidsStatus.BUS_STATION_ARRIVED
    final override fun getContext() = context
}