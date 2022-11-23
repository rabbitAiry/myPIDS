package com.airy.mypids.utils

import android.content.Context

object UnitUtil {
    fun dp2px(context: Context, dp: Int): Int{
        val scale = context.resources.displayMetrics.scaledDensity
        return (dp*scale+0.5f).toInt()
    }

    fun px2dp(context: Context, px: Int): Int{
        val scale = context.resources.displayMetrics.scaledDensity
        return (px/scale+0.5f).toInt()
    }
}