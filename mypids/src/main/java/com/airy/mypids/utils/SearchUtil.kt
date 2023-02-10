package com.airy.mypids.utils

import com.baidu.mapapi.search.busline.BusLineResult
import com.baidu.mapapi.search.busline.BusLineSearch
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener
import com.baidu.mapapi.search.poi.*

object SearchUtil {
    private var mPoiSearch: PoiSearch? = null
    private var mBusLineSearch: BusLineSearch? = null

    fun initSearches(mSearchListener: OnGetPoiSearchResultListener, mBusLineSearchListener: OnGetBusLineSearchResultListener){
        mPoiSearch = PoiSearch.newInstance()
        mPoiSearch!!.setOnGetPoiSearchResultListener(mSearchListener)
        mBusLineSearch = BusLineSearch.newInstance()
        mBusLineSearch!!.setOnGetBusLineSearchResultListener(mBusLineSearchListener)
        // com.baidu.platform.core.busline.a
    }

    fun releaseSearches() {
        mPoiSearch?.destroy()
        mBusLineSearch?.destroy()
    }

    fun getPoiSearch() = mPoiSearch!!
    fun getBusLineSearch() = mBusLineSearch!!
}