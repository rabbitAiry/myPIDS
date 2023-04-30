package com.airy.buspids.repository

import android.util.Log
import com.airy.pids_lib.constants.BAIDU_MAP_AK
import com.airy.pids_lib.data.RawBaiduRoute
import com.airy.pids_lib.data.Station
import com.airy.pids_lib.utils.responseViaOkHttp
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import java.text.DecimalFormat
import javax.inject.Inject

private const val httpsSite = "https://api.map.baidu.com/directionlite/v1/driving"
private const val TAG = "ForecastRepository"
const val STATION_STOP_MINUTE = 1

class ForecastRepository @Inject constructor(){
    private val gson = Gson()
    private val formatter = DecimalFormat("#.000000")

    /**
     * 分钟为单位
     * [currIdx] 当前到站idx
     */
    suspend fun queryOfStations(currIdx: Int, lastIdx: Int, stations: List<Station>): List<Int> {
        return withContext(Dispatchers.IO){
            val list: MutableList<Int> = MutableList(stations.size) { 0 }
            var emulated = STATION_STOP_MINUTE
            for (idx in currIdx until lastIdx){
                try {
                    emulated += queryStationSection(stations[idx], stations[idx+1])
                    list[idx+1] = emulated
                    emulated+ STATION_STOP_MINUTE
                }catch (e: Exception){
                    Log.e(TAG, "queryOfStations: $e")
                    if (e is CancellationException) throw e
                }
            }
            list
        }
    }

    private fun queryStationSection(from: Station, to: Station): Int{
        val fromLat = formatter.format(from.latitude)
        val fromLon = formatter.format(from.longitude)
        val toLat = formatter.format(to.latitude)
        val toLon = formatter.format(to.longitude)
        val url = "$httpsSite?origin=${fromLat},${fromLon}&destination=${toLat},${toLon}&ak=${BAIDU_MAP_AK}"
        val rawResult = gson.fromJson(responseViaOkHttp(url), RawBaiduRoute::class.java)
        return rawResult.result?.duration ?: throw NoSuchElementException("接口调用失败： ${rawResult.message}")
    }
}