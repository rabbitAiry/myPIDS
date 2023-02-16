package com.airy.mypids.data

data class RawPoiJson(
    val message: String,
    val results: List<RawPoiListItem>? = null
)

data class RawPoiListItem(
    val name: String,
    val uid: String,
    val detail_info: RawDetail
)

data class RawDetail(
    val tag: String?
)