package com.airy.pids_lib.utils

import android.content.Context

fun Int.pxToDp(context: Context): Int{
    val scale = context.resources.displayMetrics.scaledDensity
    return (this/scale+0.5f).toInt()
}