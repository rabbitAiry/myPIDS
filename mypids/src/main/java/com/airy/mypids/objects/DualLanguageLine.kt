package com.airy.mypids.objects

import android.os.Parcel

class DualLanguageLine : Line {
    private constructor(
        lineName: String,
        directionName: String,
        stations: List<Station>,
        currStationIdx: Int = 0,
    ) : super(lineName, directionName, stations, currStationIdx)

    constructor(parcel: Parcel) : super(parcel)

    constructor(line: Line) : super(line){
        this.line = line
    }
}