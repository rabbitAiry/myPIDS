package com.airy.pids_lib.utils

import okhttp3.OkHttpClient
import okhttp3.Request

fun responseViaOkHttp(url: String): String {
    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()
    return client.newCall(request).execute().body.string()
}