package com.airy.mypids.objects

import android.os.Parcel
import android.os.Parcelable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

data class Line(
    val lineName: String,
    val directionName: String,
    val stations: List<Station>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Station)!!
    ) {
        currStationIdx = parcel.readInt()
    }

    var currStationIdx = 0
    fun getShortLineName(): String{
        val name = lineName
        val n = name.length
        if(name[n-1] =='路'){
            val builder = StringBuilder(name)
            builder.deleteCharAt(n-1)
            return builder.toString()
        }
        return name
    }
    fun getFirstStation() = stations[0]
    fun getLastStation() = stations[stations.size - 1]
    fun getLineDescription() =
        StringBuilder().append(lineName).append(" ").append(directionName).append("方向").toString()

    fun getStationNames(): List<String> {
        val list = LinkedList<String>()
        for (station in stations) list.add(station.name)
        return list
    }

    /**
     * 获取当前到站或者下一站
     */
    fun getCurrStationName() = stations[currStationIdx].name
    fun getStation(idx: Int) = stations[idx]
    fun getLastStationIdx() = stations.size-1
    fun isLastStation() = currStationIdx==getLastStationIdx()
    fun getLineStationCount() = stations.size

    /**
     * 切换到下一站，并且返回currStationIdx
     */
    fun nextStation(): Int {
        if (currStationIdx + 1 < stations.size) currStationIdx++
        return currStationIdx
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(lineName)
        parcel.writeString(directionName)
        parcel.writeTypedList(stations)
        parcel.writeInt(currStationIdx)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Line> {
        override fun createFromParcel(parcel: Parcel): Line {
            return Line(parcel)
        }

        override fun newArray(size: Int): Array<Line?> {
            return arrayOfNulls(size)
        }
    }
}