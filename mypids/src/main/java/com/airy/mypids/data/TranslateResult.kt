package com.airy.mypids.data

data class TranslateResult(val from: String, val to: String, val trans_result: List<TextResult>, val error_code: String, val error_message: String)

data class TextResult(val src: String, val dst: String)