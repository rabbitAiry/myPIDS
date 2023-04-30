package com.airy.buspids.data

data class RawTranslateResult(
    val from: String,
    val to: String,
    val trans_result: List<TextResult>,
    val error_code: String,
    val error_message: String
)

data class TextResult(val src: String, val dst: String)