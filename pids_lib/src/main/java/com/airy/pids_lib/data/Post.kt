package com.airy.pids_lib.data

/**
 * 表示在pids中播报的宣传内容
 * [content] 内容
 * [postMillis] 每次宣传展示时长，以秒为单位
 */
data class Post(
    val content: String,
    val postMillis: Int
)