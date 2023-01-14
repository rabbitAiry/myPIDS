package com.airy.mypids.pids

import android.content.Context
import com.airy.mypids.objects.LineInfo
import com.airy.mypids.pids.gz_bus_style.GZBusStyleFragment
import com.airy.mypids.pids.vertical_style.VerticalPidsFragment

object PidsManager {
    private val pidsNameMap : HashMap<String, PidsInfo> = hashMapOf(
        "基础竖向风格" to PidsInfo(VerticalPidsFragment::class.java, false),
        "广州公交风格" to PidsInfo(GZBusStyleFragment::class.java, true)
    )

    /**
     * list元素数量不能为0，否则会带来许多错误
     */
    fun getPidsNameList() = ArrayList(pidsNameMap.keys)

    fun getPidsFragment(styleName: String, context: Context, lineInfo: LineInfo): BasePidsFragment?{
        return pidsNameMap[styleName]?.clazz?.getDeclaredConstructor(Context::class.java, LineInfo::class.java)
            ?.newInstance(context, lineInfo)
    }

    fun getIsHorizontal(styleName: String) = pidsNameMap[styleName]?.isHorizontal

    data class PidsInfo(
        val clazz: Class<out BasePidsFragment>,
        val isHorizontal: Boolean
    )
}