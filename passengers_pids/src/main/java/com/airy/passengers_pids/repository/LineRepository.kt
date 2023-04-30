package com.airy.passengers_pids.repository

import android.util.Log
import com.airy.passengers_pids.data.RawBaiduMapLineJson
import com.airy.passengers_pids.data.RawPoiJson
import com.airy.pids_lib.utils.responseViaOkHttp
import com.baidu.mapapi.search.busline.BusLineSearchOption
import com.baidu.mapapi.search.poi.PoiCitySearchOption
import com.baidu.platform.base.SearchType
import com.baidu.platform.core.busline.a
import com.baidu.platform.core.busline.b
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Constructor
import com.baidu.platform.core.d.g
import com.baidu.platform.core.d.i

private const val TAG = "LineRepository"
object LineRepository {
    /* with flow
        var poiResultList: List<PoiItem>? = null
        var line: LineInfo? = null

        private val _errors = MutableSharedFlow<String>()
        val errors: SharedFlow<String>
            get() = _errors.asSharedFlow()

        suspend fun searchLinePoi(city: String, line: String): List<PoiItem>?{
        if (poiResultList!=null) return poiResultList
        return withContext(Dispatchers.IO) {
            val url = getPoiLineSearchUrl(city, line)
            try {
                val rawPoiJson = Gson().fromJson(responseViaOkHttp(url), RawPoiJson::class.java)
                if (rawPoiJson == null){
                    _errors.emit("无法搜索")
                } else if (rawPoiJson.results == null){
                    _errors.emit("搜索错误: ${rawPoiJson.message}")
                } else {
                    poiResultList = rawPoiJson.results.filter {
                        val tag = it.detail_info.tag
                        tag?.contains("地铁线路") ?:false || tag?.contains("公交线路") ?:false
                    }
                }
                poiResultList
            } catch (e: Exception) {
                _errors.emit("无法搜索")
                Log.e(TAG, "linePoiSearch Cannot Search Error: $e")
                null
            }
        }
    }
     */

    suspend fun searchLinePoi(city: String, line: String): RawPoiJson?{
        return withContext(Dispatchers.IO) {
            val url = getPoiLineSearchUrl(city, line)
            var result: RawPoiJson? = null
            try {
                result = Gson().fromJson(responseViaOkHttp(url), RawPoiJson::class.java)
            } catch (e: Exception) {
                Log.e(TAG, "linePoiSearch Cannot Search Error: $e")
            }
            result
        }
    }

    suspend fun searchLine(cityText: String, uid: String): RawBaiduMapLineJson? {
        return withContext(Dispatchers.IO) {
            val url = getLineSearchUrl(cityText, uid)
            var result: RawBaiduMapLineJson? = null
            try {
                result = Gson().fromJson(responseViaOkHttp(url), RawBaiduMapLineJson::class.java)
            } catch (e: Exception) {
                Log.e(TAG, "lineSearch Cannot Search Error: $e")
            }
            result
        }
    }

    private fun getPoiLineSearchUrl(cityText: String, lineText: String): String {
        val option = PoiCitySearchOption().city(cityText).keyword("$lineText,公交线路,地铁线路").scope(2)
            .pageCapacity(150)
        val parser = newInstanceG(option.mPageNum, option.mPageCapacity)
        parser.a(SearchType.c)
        val req = newInstanceI(option)
        return req.a(parser.a())
    }

    private fun getLineSearchUrl(cityText: String, uid: String): String {
        val option = BusLineSearchOption().city(cityText).uid(uid)
        val parser = a()
        parser.a(SearchType.o)
        val req = b(option)
        return req.a(parser.a())
    }

    /**
     * 反射获取g类
     * 该类大概用于表示通过搜索参数设置的数据结构
     */
    private fun newInstanceG(pageIndex: Int, pageCapacity: Int): g {
        val constructor: Constructor<g> = g::class.java.getDeclaredConstructor(Int::class.java, Int::class.java)
        constructor.isAccessible = true
        return constructor.newInstance(pageIndex, pageCapacity)
    }

    /**
     * 反射获取i类
     * 该类大概用于生成URL
     */
    private fun newInstanceI(option: PoiCitySearchOption): i {
        val constructor: Constructor<i> = i::class.java.getDeclaredConstructor(PoiCitySearchOption::class.java)
        constructor.isAccessible = true
        return constructor.newInstance(option)
    }
}