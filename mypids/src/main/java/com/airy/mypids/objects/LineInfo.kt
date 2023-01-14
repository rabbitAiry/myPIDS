package com.airy.mypids.objects

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class LineInfo(
    val lineName: String,
    val lineColor: String,
    var stations: List<Station>,
    var currIdx: Int = 0,
    val lineId: String = "",
    val lineLanguageCount: Int = 1,
    val stationIdStartsFrom: Int = 0,
    val stationIdIncrease: Boolean = true,
    val lineMapStartFromLeft: Boolean = true,
    val otherLineColors: Map<String, String>? = null,
    val carriageInfo: CarriageInfo = CarriageInfo(),
    val broadcastInfo: BroadcastInfo? = null
) {
    // name style
    val briefLineName
        get() = if (lineName.last() == '路') lineName.substring(0, lineName.length - 1) else lineName

    val lineDescription get() = "$lineName ${terminusStation}方向"

    // station
    val startStation get() = stations[0]

    val terminusStation get() = stations.last()

    var currStation
        get() = stations[currIdx]
        set(value) {
            currIdx = stations.indexOf(value)
        }

    val isLastStation get() = currIdx == stations.size - 1

    val stationNames
        get() = run {
            val list = LinkedList<String>()
            for (station in stations) list.add(station.name)
            list
        }

    fun nextStation(): Int {
        if (currIdx + 1 < stations.size) currIdx++
        return currIdx
    }
}


//    : Parcelable{
//    constructor(parcel: Parcel) : this(
//        parcel.readString()!!,
//        parcel.readString()!!,
//        parcel.createTypedArrayList(Station)!!,
//        parcel.readInt()
//    ) {
//        currIdx = parcel.readInt()
//    }
//
//    constructor(l: LineInfo) : this(l.lineName, l.directionName, l.stations, l.currIdx)
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(lineName)
//        parcel.writeString(directionName)
//        parcel.writeTypedList(stations)
//        parcel.writeInt(currIdx)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<LineInfo> {
//        override fun createFromParcel(parcel: Parcel): LineInfo {
//            return LineInfo(parcel)
//        }
//
//        override fun newArray(size: Int): Array<LineInfo?> {
//            return arrayOfNulls(size)
//        }
//    }
//
//    // -- Parcel part end --
//
//}