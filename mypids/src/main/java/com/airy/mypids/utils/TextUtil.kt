package com.airy.mypids.utils

import android.util.Log

object TextUtil {

    private const val TAG = "TextUtil"
    /**
     * 从线路描述中提取线路名称
     *
     * 如：输入"地铁3号线(天河客运站-番禺广场)"则返回"3号线"
     */
    fun getRawLineName(lineName: String): String{
        Log.d(TAG, "getRawLineName: $lineName")
        val start = if (lineName.startsWith("地铁")) 2 else 0
        for (i in lineName.indices){
            if(lineName[i]=='('){
                Log.d(TAG, "getRawLineName: ${lineName.substring(start, i)}")
                return lineName.substring(start, i)
            }
        }
        return lineName
    }

    /**
     * 从HTML格式标签中获取值
     *
     * 如: 输入"<font color=\"#ffffff\">5号线<\/font>"，则返回"5号线"
     */
    fun getTagContent(rawText: String):String{
        var hasTag = false
        var start = 0
        var end = rawText.length
        for(i in rawText.indices){
            if (!hasTag && rawText[i]=='<'){
                hasTag = true
            } else if (hasTag && rawText[i]=='<'){
                end = i
                break
            } else if(hasTag && rawText[i] == '>'){
                start = i+1
            }
        }
        return rawText.substring(start, end)
    }
}