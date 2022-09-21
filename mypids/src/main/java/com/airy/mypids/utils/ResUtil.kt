package com.airy.mypids.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import com.airy.mypids.R

@SuppressLint("UseCompatLoadingForDrawables")
object ResUtil {
    fun getResColor(resId: Int, context: Context): Int{
        return context.resources.getColor(resId, context.theme)
    }

    fun getDrawable(resId: Int, context: Context):Drawable{
        return context.resources.getDrawable(resId, context.theme)
    }
}