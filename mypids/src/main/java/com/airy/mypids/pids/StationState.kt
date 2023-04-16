package com.airy.mypids.pids

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.airy.mypids.ui.theme.light_blue_100
import com.airy.mypids.ui.theme.light_blue_400
import com.airy.mypids.ui.theme.light_blue_800
import kotlinx.coroutines.CoroutineScope

enum class StationStatus {
    ARRIVED, CURR_ARRIVED, CURR_UNREACHED, UNREACHED
}