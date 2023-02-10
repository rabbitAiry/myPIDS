package com.airy.mypids.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.airy.mypids.objects.*
import com.airy.mypids.repository.LineRepository
import com.airy.mypids.repository.PoiLineRepository
import com.airy.mypids.utils.ColorUtil
import com.airy.mypids.utils.TextUtil
import com.baidu.mapapi.model.CoordUtil
import kotlinx.coroutines.launch

private const val TAG = "PidsViewModel"
class PidsViewModel(application: Application) : AndroidViewModel(application) {
    var lineInfo: LineInfo? by mutableStateOf(null)
    var stationListInfo: StationListInfo? = null



    fun nonNullCheck(): Boolean {
        return lineInfo != null && stationListInfo != null
    }

    fun clearInfo(){
        lineInfo = null
        stationListInfo = null
    }
}