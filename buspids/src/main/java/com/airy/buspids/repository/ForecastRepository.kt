package com.airy.buspids.repository

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.airy.pids_lib.constants.BAIDU_MAP_AK
import com.airy.pids_lib.data.RawBaiduRouteMessage
import com.airy.pids_lib.data.Station
import com.airy.pids_lib.utils.responseViaOkHttp
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import javax.inject.Inject

private const val httpsSite = "https://api.map.baidu.com/directionlite/v1/driving"
private const val TAG = "ForecastRepository"
const val STATION_STOP_SECOND = 30

class ForecastRepository @Inject constructor() {
    private val gson = Gson()
    private val formatter = DecimalFormat("#.000000")

    /**
     * 分钟为单位
     * [currIdx] 当前到站idx
     */
    suspend fun queryOfStations(
        currIdx: Int,
        lastIdx: Int,
        stations: List<Station>
    ): List<Int>? {
        return withContext(Dispatchers.IO) {
            val list = MutableList(stations.size){0}
            var emulated = STATION_STOP_SECOND + 60
            for (idx in currIdx until lastIdx) {
                try {
                    emulated += queryStationSection(stations[idx], stations[idx + 1])
                    list[idx + 1] = emulated
                    logPosition("前往${stations[idx+1].name}: ${list[idx+1]/60}分钟")
                    emulated += STATION_STOP_SECOND
                } catch (e: Exception) {
                    Log.e(TAG, "queryOfStations: $e")
                    if (e is CancellationException) throw e
                    return@withContext null
                }
            }
            list
        }
    }

    suspend fun queryOfStationsOfPosition(
        latitude: Double?,
        longitude: Double?,
        currIdx: Int,
        lastIdx: Int,
        stations: List<Station>
    ) : List<Int>? {
        if (latitude == null || longitude == null) return null
        return withContext(Dispatchers.IO) {
            val list = MutableList(stations.size){0}
            var emulated = 60
            emulated += queryPositionStationSection(latitude, longitude, stations[currIdx])
            list[currIdx] = emulated
            logPosition("前往${stations[currIdx].name}: ${list[currIdx]/60}分钟")
            for (idx in currIdx until lastIdx) {
                try {
                    emulated += queryStationSection(stations[idx], stations[idx + 1])
                    list[idx + 1] = emulated
                    logPosition("前往${stations[idx+1].name}: ${list[idx+1]/60}分钟")
                    emulated += STATION_STOP_SECOND
                } catch (e: Exception) {
                    Log.e(TAG, "queryOfStations: $e")
                    if (e is CancellationException) throw e
                }
            }
            list
        }
    }

    /**
     * 返回单位为秒
     */
    private fun queryStationSection(from: Station, to: Station): Int {
        val fromLat = formatter.format(from.latitude)
        val fromLon = formatter.format(from.longitude)
        val toLat = formatter.format(to.latitude)
        val toLon = formatter.format(to.longitude)
        val url =
            "$httpsSite?origin=${fromLat},${fromLon}&destination=${toLat},${toLon}&ak=${BAIDU_MAP_AK}"
        val rawResult = gson.fromJson(responseViaOkHttp(url), RawBaiduRouteMessage::class.java)
        return rawResult.result?.routes?.get(0)?.duration
            ?: throw NoSuchElementException("接口调用失败： ${rawResult.message}")
    }

    /**
     * 返回单位为秒
     */
    private fun queryPositionStationSection(_fromLat: Double, _fromLon: Double, to: Station): Int {
        val fromLat = formatter.format(_fromLat)
        val fromLon = formatter.format(_fromLon)
        val toLat = formatter.format(to.latitude)
        val toLon = formatter.format(to.longitude)
        val url =
            "$httpsSite?origin=${fromLat},${fromLon}&destination=${toLat},${toLon}&ak=${BAIDU_MAP_AK}"
        val rawResult = gson.fromJson(responseViaOkHttp(url), RawBaiduRouteMessage::class.java)
        return rawResult.result?.routes?.get(0)?.duration
            ?: throw NoSuchElementException("接口调用失败： ${rawResult.message}")
    }
}