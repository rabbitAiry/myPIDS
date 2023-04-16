package com.airy.buspids.vm

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.airy.buspids.data.LineState
import com.airy.pids_lib.data.*
import com.airy.pids_lib.fake_data.lineInfoB8

/**
 * 宣传内容会随着每次发车时全量获取；行驶期间会接受新的宣传内容；每次介素行车后清空
 */
class PidsViewModel : ViewModel() {
    val line: LineState
    val postList: List<Post>

    init {
        line = LineState(lineInfoB8)
        postList = listOf(
            NormalPost("尊老爱幼是中华民族的传统美德，请您为有需要的乘客让座，谢谢！", 20000),
            StationPost("灯光节展会", 10000, 15)
        )
    }
}