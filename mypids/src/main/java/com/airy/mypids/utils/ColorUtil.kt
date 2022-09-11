package com.airy.mypids.utils

import android.content.Context

object ColorUtil {
    fun getResColor(resId: Int, context: Context): Int{
        return context.resources.getColor(resId, context.theme)
    }
}