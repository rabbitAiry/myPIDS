package com.airy.pids_lib.data

data class RawBaiduRouteMessage(
    val status: Int,
    val message: String,
    val result: RawBaiduResult?
)

data class RawBaiduResult(
    val routes: List<RawBaiduRoute>?
)

data class RawBaiduRoute(
    val duration: Int
)