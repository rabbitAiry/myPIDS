package com.airy.mypids.repository

import android.util.Log
import com.airy.mypids.data.RawBaiduMapLineJson
import com.airy.mypids.utils.responseViaOkHttp
import com.baidu.mapapi.search.busline.BusLineSearch
import com.baidu.mapapi.search.busline.BusLineSearchOption
import com.baidu.platform.base.SearchType
import com.baidu.platform.core.busline.a
import com.baidu.platform.core.busline.b
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "BaiduMapLineRepository"

class LineRepository {
    private val mBusLineSearch = BusLineSearch.newInstance()

    suspend fun lineSearch(cityText: String, uid: String): RawBaiduMapLineJson? {
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

    private fun getLineSearchUrl(cityText: String, uid: String): String {
        val option = BusLineSearchOption().city(cityText).uid(uid)
        val parser = a()
        parser.a(SearchType.o)
        val req = b(option)
        return req.a(parser.a())
    }

    fun free() {
        mBusLineSearch.destroy()
    }
}