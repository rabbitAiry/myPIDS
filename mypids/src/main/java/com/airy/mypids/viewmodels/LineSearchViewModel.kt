package com.airy.mypids.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airy.mypids.objects.*
import com.airy.mypids.repository.LineRepository
import com.airy.mypids.repository.PoiLineRepository
import com.airy.mypids.utils.ColorUtil
import com.airy.mypids.utils.TextUtil
import com.baidu.mapapi.model.CoordUtil
import kotlinx.coroutines.launch

private const val TAG = "LineSearchViewModel"

class LineSearchViewModel : ViewModel() {
    private val poiLineRepository = PoiLineRepository()
    private val lineRepository = LineRepository()

    var searchCity by mutableStateOf("广州")  // TODO
    var searchText by mutableStateOf("B4A") // TODO
    var poiSearchError: String? = null
    var lineSearchResult: String? = null
    var resultPoiList: List<RawPoiListItem>? = null
    var inPoiSearch by mutableStateOf(false)   // 包括搜索和筛选的过程

    fun searchTextNotNull(): Boolean = searchCity.isNotEmpty() && searchText.isNotEmpty()

    fun getLinePoiSearch() {
        viewModelScope.launch {
            poiSearchError = null
            resultPoiList = null
            inPoiSearch = true
            val rawPoiJson = poiLineRepository.linePoiSearch(searchCity, searchText)
            if (rawPoiJson == null) {
                poiSearchError = "无法搜索"
            } else if (rawPoiJson.results == null) {
                poiSearchError = "搜索错误：${rawPoiJson.message}"
            } else {
                resultPoiList = rawPoiJson.results
                resultPoiList!!.filter {
                    val tag = it.detail_info.tag
                    tag.contains("地铁线路") || tag.contains("公交线路")
                }
            }
            inPoiSearch = false
        }
    }

    fun getLineSearch(uid: String, onLineGot: (LineInfo, StationListInfo)->Unit){
        viewModelScope.launch {
            val rawBaiduMapJson = lineRepository.lineSearch(searchCity, uid)
            rawBaiduMapJson?.let { json ->
                if (json.bsl?.content?.get(0) != null) {
                    collectLineData(json.bsl.content[0], onLineGot)
                } else if (json.result?.errMsg != null) "error: ${json.result.errMsg}"
                else if (json.message != null) "error: ${json.message}"
                else "error: 无法获取线路数据"
            }
        }
    }

    private fun collectLineData(
        rawLine: RawBaiduMapLine,
        onLineGot: (LineInfo, StationListInfo) -> Unit
    ) {
        rawLine.run {
            val stationList: ArrayList<Station> = ArrayList()
            val otherLineColors: HashMap<String, Color> = HashMap()
            for (station in stations){
                val location = CoordUtil.decodeLocation(station.geo)
                stationList.add(Station(
                    names = listOf(station.name),
                    latitude = location.latitude,
                    longitude = location.longitude,
                    interchanges = station.subways?.map { TextUtil.getTagContent(it.name) }
                ))
                station.subways?.let{
                    for (subway in it){
                        otherLineColors[TextUtil.getTagContent(subway.name)] = ColorUtil.parseColor(
                            subway.background_color
                        ) ?: Color.White
                    }
                }
            }
            val stationListInfo = StationListInfo(
                stations = stationList,
                languages = listOf("zh", "en")
            )
            val lineInfo = LineInfo(
                lineName = name,
                rawLineName = TextUtil.getRawLineName(name),
                lineDirection = stationList.last().name,
                lineColor = ColorUtil.parseColor(line_color),
                otherLineColors = otherLineColors
            )
            onLineGot(lineInfo, stationListInfo)
        }
    }

    override fun onCleared() {
        super.onCleared()
        lineRepository.free()
        poiLineRepository.free()
    }
}