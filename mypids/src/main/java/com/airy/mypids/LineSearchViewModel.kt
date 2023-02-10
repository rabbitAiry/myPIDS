package com.airy.mypids
/*
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.airy.mypids.utils.SearchUtil
import com.baidu.mapapi.search.busline.BusLineResult
import com.baidu.mapapi.search.busline.BusLineSearchOption
import com.baidu.mapapi.search.core.PoiInfo
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.poi.*
import java.util.*

private const val TAG = "LineSearchViewModel"
class LineSearchViewModel : ViewModel() {


    val pidsOptionList = PidsManager.getPidsNameList()
    var styleName: String by mutableStateOf(pidsOptionList[0])
    var pidsInfo: PidsManager.PidsInfo? by mutableStateOf(null)
    var resultList: List<PoiInfo>? by mutableStateOf(null)
    var status by mutableStateOf(LineConfigurationStatus.NOT_CHOOSE)
    var lastSearch by mutableStateOf("")

    init {
        SearchUtil.initSearches(
            mSearchListener = object : OnGetPoiSearchResultListener {
                override fun onGetPoiResult(p0: PoiResult?) {
                    val list = LinkedList<PoiInfo>()
                    if (p0 == null || p0.error != SearchResult.ERRORNO.NO_ERROR) {
                        Log.d(TAG, "onGetPoiResult: 搜索过程出现问题${p0?.error?.name}")
                    } else {
                        for (info in p0.allPoi) {
                            val tag = info.getPoiDetailInfo().tag
                            if (tag.contains("地铁线路") || tag.contains("公交线路")) list.add(info)
                        }
                    }
                    resultList = list
                }

                @Deprecated("Deprecated in Java")
                override fun onGetPoiDetailResult(p0: PoiDetailResult?) {
                }

                override fun onGetPoiDetailResult(p0: PoiDetailSearchResult?) {}
                override fun onGetPoiIndoorResult(p0: PoiIndoorResult?) {}
            },

            mBusLineSearchListener = { p0: BusLineResult? ->
                if (p0 == null || p0.error != SearchResult.ERRORNO.NO_ERROR) {
                    Log.d(TAG, "查找线路出错：${p0?.error?.name}")
                } else {
                    try {
                        pidsInfo = LineUtil.baiduLineToAppLine(p0)
                    } catch (e: NullPointerException) {
                        Log.d(TAG, "数据缺失")
                    }
                }
                status = LineConfigurationStatus.CHOSEN
            }
        )
    }

    /**
     * 是否得到足够的数据启动PIDS
     */
    fun isPidsReady() = pidsInfo != null

    fun onLineSearch(cityText: String, lineText: String) {
        status = LineConfigurationStatus.IN_CHOOSE
        resultList = null
        SearchUtil.getPoiSearch().searchInCity(
            PoiCitySearchOption().city(cityText).keyword(lineText).scope(2).pageCapacity(1000)
        )
    }

    fun onLineSelected(info: PoiInfo) {
        resultList = null       // 为了展现ProgressBar
        SearchUtil.getBusLineSearch().searchBusLine(
            BusLineSearchOption().city(info.city).uid(info.uid)
        )
    }

    override fun onCleared() {
        SearchUtil.releaseSearches()
        super.onCleared()
    }
}

 */