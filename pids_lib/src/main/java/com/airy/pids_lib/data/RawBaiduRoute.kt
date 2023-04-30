package com.airy.pids_lib.data

data class RawBaiduRoute(
    val status: Int,
    val message: String,
    val result: RawBaiduResult?
)

data class RawBaiduResult(
    val duration: Int
)