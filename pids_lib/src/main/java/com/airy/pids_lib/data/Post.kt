package com.airy.pids_lib.data

/**
 * 表示在pids中播报的宣传内容
 * [content] 内容
 * [duration] 每次宣传展示时长，以秒为单位
 */
interface Post {
    val content: String
    val duration: Int
}

class NormalPost(
    override val content: String,
    override val duration: Int
) : Post

/**
 * 表示这个宣传内容应该只会在第idx个站点之前被展示
 * 通常用于诸如“前往灯光节会场可在天河站下车”这种与站点相关的宣传
 */
class StationPost(
    override val content: String,
    override val duration: Int,
    val stationIdxBefore: Int
) : Post