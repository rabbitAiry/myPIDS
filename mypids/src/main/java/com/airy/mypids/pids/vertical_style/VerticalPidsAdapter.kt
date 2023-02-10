package com.airy.mypids.pids.vertical_style

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.airy.mypids.R
import com.airy.mypids.utils.ResUtil

class VerticalPidsAdapter(
    var stationNames: List<String>,
    val context: Context,
    private var currStationIdx: Int
) : RecyclerView.Adapter<VerticalPidsAdapter.VerticalPidsHolder>() {
    private val handler = Handler(Looper.getMainLooper())
    private var statusRunning = false
    private var shiningOn = false

    class VerticalPidsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stationStatus: View = itemView.findViewById(R.id.view_station_status)
        val stationName: TextView = itemView.findViewById(R.id.text_station)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalPidsHolder {
        return VerticalPidsHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_station_basic, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VerticalPidsHolder, position: Int) {
        val pos = holder.adapterPosition
        val shapeId =
            if (pos < currStationIdx || (pos == currStationIdx && (shiningOn || !statusRunning))) R.drawable.station_arrived_round else R.drawable.station_unarrive_round
        holder.stationStatus.background = AppCompatResources.getDrawable(context, shapeId)
        holder.stationName.text = stationNames[pos]
        val backgroundColorId = if(pos == currStationIdx) R.color.light_blue_100 else R.color.white
        holder.itemView.setBackgroundColor(ResUtil.getResColor(backgroundColorId, context))
    }

    override fun getItemCount(): Int = stationNames.size

    fun nextStation() {
        currStationIdx++
        handler.removeCallbacksAndMessages(null)
        notifyItemChanged(currStationIdx-1)
        if(statusRunning)shine()
    }

    fun stationArrived(){
        statusRunning = false
        notifyItemChanged(currStationIdx)
        handler.removeCallbacksAndMessages(null)
    }

    fun busRunning(){
        statusRunning = true
        shine()
    }

    private fun shine(){
        shiningOn = !shiningOn
        notifyItemChanged(currStationIdx)
        handler.postDelayed({ shine() }, 500)
    }
}