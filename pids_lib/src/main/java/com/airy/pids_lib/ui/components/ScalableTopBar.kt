package com.airy.pids_lib.ui.components

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airy.pids_lib.utils.pxToDp

@Composable
fun ScalableTopBar(
    title: String,
    isMain: Boolean = false,
    maxHeight: Int = 240,
    minHeight: Int = 80,
    scrollState: ScrollState,
    context: Context
) {
    val backgroundColor =
        if (isMain) MaterialTheme.colors.primary else MaterialTheme.colors.secondary
    val loss = (maxHeight - minHeight).coerceAtMost(scrollState.value.pxToDp(context))
    val rate = (if (isMain) 1f else 0.8f)*(maxHeight - loss) / maxHeight
    val boxHeight = maxHeight - loss
    val textSize = 48 * rate
    val bottomPadding = if (boxHeight == minHeight) 10 else 24
    Box(
        modifier = Modifier
            .padding(bottom = 30.dp)
            .fillMaxWidth()
            .height(boxHeight.dp)
            .background(backgroundColor)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h3,
            fontSize = textSize.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = bottomPadding.dp)
        )
    }
}