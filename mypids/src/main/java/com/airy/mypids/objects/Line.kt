package com.airy.mypids.objects

import android.os.Parcel
import android.os.Parcelable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

data class Line(
    val lineName: String,
    val directionName: String,
    var stations: List<Station>,
    var currStationIdx: Int = 0,
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Station)!!,
        parcel.readInt()
    ) {
        currStationIdx = parcel.readInt()
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

    val firstStation get() = stations[0]
    val lastStation get() = stations[stations.size - 1]
    val currStation get() = stations[currStationIdx]
    val lineDescription get() = "$lineName ${directionName}方向"
    val isLastStation get() = currStationIdx==stations.size-1
    val stationCount get() = stations.size
    val briefLineName get() = run {
        val name = lineName
        val n = name.length
        if(name[n-1] =='路'){
            val builder = StringBuilder(name)
            builder.deleteCharAt(n-1)
            builder.toString()
        }
        name
    }

    val stationNames get() = run{
        val list = LinkedList<String>()
        for (station in stations) list.add(station.name)
        list
    }

    fun getStation(idx: Int) = stations[idx]

    fun setCurrStation(station: Station){
        currStationIdx = stations.indexOf(station)
    }


    /**
     * 切换到下一站，并且返回currStationIdx
     */
    fun nextStation(): Int {
        if (currStationIdx + 1 < stations.size) currStationIdx++
        return currStationIdx
    }
}