package com.airy.mypids.repository

import android.util.Log
import com.airy.mypids.data.RawPoiJson
import com.airy.mypids.utils.responseViaOkHttp
import com.baidu.mapapi.search.poi.PoiCitySearchOption
import com.baidu.mapapi.search.poi.PoiSearch
import com.baidu.platform.base.SearchType
import com.baidu.platform.core.d.g
import com.baidu.platform.core.d.i
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Constructor

private const val TAG = "PoiRepository"
class PoiLineRepository {
    private val mPoiSearch = PoiSearch.newInstance()

    suspend fun linePoiSearch(cityText: String, lineText: String): RawPoiJson? {
        return withContext(Dispatchers.IO) {
            val url = getPoiLineSearchUrl(cityText, lineText)
            var result: RawPoiJson? = null
            try {
                result = Gson().fromJson(responseViaOkHttp(url), RawPoiJson::class.java)
            } catch (e: Exception) {
                Log.e(TAG, "linePoiSearch Cannot Search Error: $e")
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

    fun free() {
        mPoiSearch.destroy()
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