package com.airy.buspids.repository

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.airy.buspids.data.RawBaiduMapLine
import com.airy.buspids.data.RawBaiduMapLineJson
import com.airy.buspids.data.RawTranslateResult
import com.airy.pids_lib.constants.BAIDU_TRANSLATE_APP_ID
import com.airy.pids_lib.constants.BAIDU_TRANSLATE_KEY
import com.airy.pids_lib.data.LineInfo
import com.airy.pids_lib.data.Station
import com.airy.pids_lib.utils.getRawLineName
import com.airy.pids_lib.utils.getTagContent
import com.airy.pids_lib.utils.parseColor
import com.airy.pids_lib.utils.responseViaOkHttp
import com.baidu.mapapi.model.CoordUtil
import com.baidu.mapapi.search.busline.BusLineSearchOption
import com.baidu.platform.base.SearchType
import com.baidu.platform.core.busline.a
import com.baidu.platform.core.busline.b
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

private const val httpsSite = "https://fanyi-api.baidu.com/api/trans/vip/translate/"
private const val TAG = "LineRepository"

@Singleton
class LineRepository @Inject constructor() {
    var lineId = ""

    private var _line: LineInfo? = null
    val line: LineInfo
        get() = _line!!

    private val _isSearchLineDone = MutableSharedFlow<Boolean>()
    val isSearchLineDone: SharedFlow<Boolean>
        get() = _isSearchLineDone.asSharedFlow()

    private val _errors = MutableSharedFlow<String>()
    val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()

    suspend fun searchLine(cityText: String, uid: String, range: IntRange): LineInfo? {
        if (_line != null) return _line
        lineId = uid
        return withContext(Dispatchers.IO) {
            val url = getLineSearchUrl(cityText, uid)
            try {
                val rawBaiduMapJson =
                    Gson().fromJson(responseViaOkHttp(url), RawBaiduMapLineJson::class.java)
                rawBaiduMapJson!!.let { json ->
                    if (json.bsl?.content?.get(0) != null) {
                        _line = collectLineData(json.bsl.content[0], range)
                        inflateEnglish(_line!!)
                        _isSearchLineDone.emit(true)
                    } else {
                        _errors.emit(
                            if (json.result?.errMsg != null) "遇到错误: ${json.result.errMsg}"
                            else if (json.message != null) "遇到错误: ${json.message}"
                            else "无法获取线路数据"
                        )
                    }
                }
                _line
            } catch (e: Exception) {
                _errors.emit("无法搜索")
                Log.e(TAG, "lineSearch Cannot Search Error: $e")
                null
            }
        }
    }

    suspend fun clearLine() {
        _line = null
        _isSearchLineDone.emit(false)
    }

    private fun getLineSearchUrl(cityText: String, uid: String): String {
        val option = BusLineSearchOption().city(cityText).uid(uid)
        val parser = a()
        parser.a(SearchType.o)
        val req = b(option)
        return req.a(parser.a())
    }

    private fun collectLineData(
        rawLine: RawBaiduMapLine,
        range: IntRange
    ): LineInfo {
        rawLine.run {
            val stationList: ArrayList<Station> = ArrayList()
            val otherLineColors: HashMap<String, Color> = HashMap()
            for (station in stations) {
                val location = CoordUtil.decodeLocation(station.geo)
                stationList.add(Station(
                    names = mutableListOf(station.name),
                    latitude = location.latitude,
                    longitude = location.longitude,
                    interchanges = station.subways?.map { getTagContent(it.name) }?.filter {
                        val last = it.substring(it.length-2, it.length)
                        last != "快线"&& last != "快车"
                    }
                ))
                station.subways?.let {
                    for (subway in it) {
                        otherLineColors[getTagContent(subway.name)] = parseColor(
                            subway.background_color
                        ) ?: Color.White
                    }
                }
            }
            val color = if (line_color != null) parseColor(line_color) else null
            return LineInfo(
                lineName = name,
                rawLineName = getRawLineName(name),
                lineDirection = stationList.last().name,
                stations = stationList,
                languages = mutableListOf("zh"),
                lineId = name,
                lineColor = color ?: Color.White,
                otherLineColors = otherLineColors,
                startStationIdx = range.first,
                endStationIdx = range.last
            )
        }
    }

    private suspend fun inflateEnglish(line: LineInfo) {
        return withContext(Dispatchers.IO) {
            val url = getURL(concatStationTexts(line.stations))
            val rawTranslateResult =
                Gson().fromJson(responseViaOkHttp(url), RawTranslateResult::class.java)
            line.languages.add("en")
            try {
                val translation = rawTranslateResult.trans_result[0].dst
                parseTextToList(translation, line.stations)
            } catch (e: Exception) {
                Log.e(TAG, "inflateEnglish: $e", )
                _errors.emit("翻译出错")
            }
        }
    }

    private fun concatStationTexts(list: List<Station>): String {
        return StringBuilder().run {
            list.forEach {
                if (it.name.last() == '站') append(it.name).append(";")
                else append(it.name).append("站;")
            }
            deleteCharAt(length - 1)
            toString()
        }
    }

    private fun getURL(text: String): String {
        val salt = Random().nextInt(100)
        val sign = getMD5(BAIDU_TRANSLATE_APP_ID + text + salt + BAIDU_TRANSLATE_KEY)
        return "$httpsSite?appid=$BAIDU_TRANSLATE_APP_ID&q=$text&from=zh&to=en&salt=$salt&sign=$sign"
    }

    private fun getMD5(text: String): String {
        val digest = MessageDigest.getInstance("MD5")
        val result = digest.digest(text.toByteArray())
        val stringBuilder = StringBuilder()

        result.forEach {
            val value = it
            val hex = value.toInt() and (0xFF)
            val hexStr = Integer.toHexString(hex)
            println(hexStr)
            if(hexStr.length == 1){
                stringBuilder.append(0).append(hexStr)
            } else {
                stringBuilder.append(hexStr)
            }
        }

        return stringBuilder.toString()
    }

    private fun parseTextToList(res: String, stations: List<Station>) {
        val list = res.split("; ")
        for ((idx, name) in list.withIndex()) {
            if (stations[idx].name.last()=='站'){
                stations[idx].names.add(name)
            } else {
                stations[idx].names.add(name.replace("Station", ""))
            }
        }
    }
}