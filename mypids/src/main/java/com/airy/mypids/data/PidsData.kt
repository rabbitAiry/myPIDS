package com.airy.mypids.data

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.airy.mypids.pids.PidsManager

private const val TAG = "LineData"
object PidsData{
    var lineInfo: LineInfo? by mutableStateOf(null)
    var stationListInfo: StationListInfo? = null
    var style :String by mutableStateOf(PidsManager.pidsNameList[0])

    fun checkLineNotNull(): Boolean{
        return lineInfo !=null && stationListInfo !=null
    }

    fun setLine(lineInfo: LineInfo, stationListInfo: StationListInfo){
        PidsData.lineInfo = lineInfo
        PidsData.stationListInfo = stationListInfo
        Log.d(TAG, "$lineInfo")
        Log.d(TAG, "$stationListInfo")
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