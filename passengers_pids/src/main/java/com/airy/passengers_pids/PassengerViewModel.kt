package com.airy.passengers_pids

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airy.passengers_pids.data.PoiItem
import com.airy.passengers_pids.data.RawBaiduMapLine
import com.airy.passengers_pids.repository.LineRepository
import com.airy.pids_lib.data.LineInfo
import com.airy.pids_lib.data.Station
import com.airy.pids_lib.utils.getRawLineName
import com.airy.pids_lib.utils.getTagContent
import com.airy.pids_lib.utils.parseColor
import com.baidu.mapapi.model.CoordUtil
import kotlinx.coroutines.launch

class PassengerViewModel : ViewModel(){
    var line: LineInfo? = null
    var resultPoiList: List<PoiItem>? = null

    var searchCityText by mutableStateOf("广州")
    var searchLineText by mutableStateOf("")

    var inPoiSearch by mutableStateOf(false)
    var inLineSearch by mutableStateOf(false)
    var poiSearchError: String? = null

    var lineSearchError: String? = null

    fun searchLinePoi(onError:()->Unit){
        val city = searchCityText
        val line = searchLineText
        if (city.isEmpty() || line.isEmpty()) {
            onError()
            return
        }
        viewModelScope.launch {
            poiSearchError = null
            resultPoiList = null
            inPoiSearch = true
            val rawPoiJson = LineRepository.searchLinePoi(city, line)
            if (rawPoiJson == null) {
                poiSearchError = "无法搜索"
            } else if (rawPoiJson.results == null) {
                poiSearchError = "搜索错误：${rawPoiJson.message}"
            } else {
                resultPoiList = rawPoiJson.results
                resultPoiList = resultPoiList!!.filter {
                    val tag = it.detail_info.tag
                    tag?.contains("地铁线路") ?:false || tag?.contains("公交线路") ?:false
                }
            }
            inPoiSearch = false
        }
    }

    fun searchLine(uid: String){
        viewModelScope.launch {
            inLineSearch = true
            line = null
            lineSearchError = null
            val rawBaiduMapJson = LineRepository.searchLine(searchCityText, uid)
            rawBaiduMapJson?.let { json ->
                if (json.bsl?.content?.get(0) != null) {
                    line = collectLineData(json.bsl.content[0])
                } else{
                    lineSearchError = if (json.result?.errMsg != null) "遇到错误: ${json.result.errMsg}"
                    else if (json.message != null) "遇到错误: ${json.message}"
                    else "无法获取线路数据"
                }
            }
            inLineSearch = false
        }
    }

    fun clearLine(){
        line = null
        resultPoiList = null
    }

    private fun collectLineData(
        rawLine: RawBaiduMapLine
    ): LineInfo {
        rawLine.run {
            val stationList: ArrayList<Station> = ArrayList()
            val otherLineColors: HashMap<String, Color> = HashMap()
            for (station in stations){
                val location = CoordUtil.decodeLocation(station.geo)
                stationList.add(Station(
                    names = listOf(station.name),
                    latitude = location.latitude,
                    longitude = location.longitude,
                    interchanges = station.subways?.map { getTagContent(it.name) }
                ))
                station.subways?.let{
                    for (subway in it){
                        otherLineColors[getTagContent(subway.name)] = parseColor(
                            subway.background_color
                        ) ?: Color.White
                    }
                }
            }
            val color = if (line_color!=null)parseColor(line_color) else null
            return LineInfo(
                lineName = name,
                rawLineName = getRawLineName(name),
                lineDirection = stationList.last().name,
                stations = stationList,
                languages = listOf("zh"),
                lineId = name,
                lineColor = color ?: Color.White,
                otherLineColors = otherLineColors
            )
        }
    }
}