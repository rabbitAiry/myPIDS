package com.airy.mypids.pids

import android.content.Context
import android.util.Log
import com.airy.mypids.data.LineInfo
import com.airy.mypids.pids.gz_bus_style.GZBusStyleFragment

private const val TAG = "PidsManager"
object PidsManager {
    private val pidsNameMap : HashMap<String, PidsInfo> = hashMapOf(
//        "基础竖向风格" to PidsInfo(VerticalPidsFragment::class.java, false),
        "广州公交风格" to PidsInfo(GZBusStyleFragment::class.java, true)
    )

    /**
     * list元素数量不能为0，否则会带来许多错误
     */
    val pidsNameList get() = ArrayList(pidsNameMap.keys)

    fun getPidsFragment(styleName: String, context: Context, lineInfo: LineInfo, stations: StationListInfo): BasePidsFragment?{
        Log.e(TAG, "getPidsFragment: ${pidsNameMap[styleName]}")
        return pidsNameMap[styleName]?.clazz?.getDeclaredConstructor(Context::class.java, LineInfo::class.java, StationListInfo::class.java)
            ?.newInstance(context, lineInfo, stations)
    }

    fun getIsHorizontal(styleName: String) = pidsNameMap[styleName]?.isHorizontal!!

    data class PidsInfo (
        val clazz: Class<out BasePidsFragment>,
        val isHorizontal: Boolean
    )
}