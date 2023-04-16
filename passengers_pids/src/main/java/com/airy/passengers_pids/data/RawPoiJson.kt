package com.airy.passengers_pids.data

data class RawPoiJson(
    val message: String,
    val results: List<PoiItem>? = null
)

data class PoiItem(
    val name: String,
    val uid: String,
    val detail_info: RawDetail
)

data class RawDetail(
    val tag: String?
)