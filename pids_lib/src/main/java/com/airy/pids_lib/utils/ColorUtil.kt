package com.airy.pids_lib.utils

import androidx.compose.ui.graphics.Color

/**
 * 将十六进制的颜色转换为Color
 * 接受“#FF0000”，“FF0000”，“FFFF0000”的形式，如果转换失败，则返回空值
 */
fun parseColor(colorText: String): Color? {
    try {
        val colorTextFormatBuilder = StringBuilder(colorText.trim())
        colorTextFormatBuilder.apply {
            if (get(0) == '#') deleteCharAt(0)
            if (length == 6) colorTextFormatBuilder.insert(0, "FF")
            if (length != 8) return null
        }
        return Color(colorTextFormatBuilder.toString().toLong(16))
    }catch (e: Exception){
        return null
    }
}