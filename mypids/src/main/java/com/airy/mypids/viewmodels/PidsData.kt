package com.airy.mypids.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.airy.mypids.data.*
import com.airy.mypids.pids.PidsManager

private const val TAG = "PidsViewModel"
object PidsData{
    var lineInfo: LineInfo? by mutableStateOf(null)
    var stationListInfo: StationListInfo? = null
    var style :String by mutableStateOf(PidsManager.pidsNameList[0])

    fun checkLineNotNull(): Boolean{
        Log.d(TAG, "checkNull: $lineInfo")
        Log.d(TAG, "checkNull: $stationListInfo")
        return lineInfo!=null && stationListInfo!=null
    }

    fun setLine(lineInfo: LineInfo, stationListInfo: StationListInfo){
        this.lineInfo = lineInfo
        this.stationListInfo = stationListInfo
    }

    fun clearLine(){
        lineInfo = null
        stationListInfo = null
    }

    fun setLineId(id: String){
        lineInfo?.lineId = id
    }

    fun setLineColor(color: Color){
        lineInfo?.lineColor = color
    }
}