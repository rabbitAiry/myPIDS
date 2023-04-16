package com.airy.pids_lib.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

/**
 * 警告风格的文字，占满一行
 */
@Composable
fun FullWarningText(text: String, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Icon(Icons.Rounded.Warning, "注意")
        Text(text, color = Color.DarkGray)
    }
}

/**
 * 警告风格的文字，占满一行
 */
@Composable
fun RowScope.RowWarningText(text: String, modifier: Modifier = Modifier) {
    Row(modifier.weight(1f).align(Alignment.CenterVertically), horizontalArrangement = Arrangement.Center) {
        Icon(Icons.Rounded.Warning, "注意")
        Text(text, color = Color.DarkGray)
    }
}

@Composable
fun SummaryText(text: String, isMain: Boolean = false) {
    Text(
        text = text,
        style = MaterialTheme.typography.h5,
        fontWeight = if (isMain) FontWeight.Medium else null
    )
}

@Composable
fun DescriptionText(text: String, modifier: Modifier) {
    Text(text = text, modifier = modifier, style = MaterialTheme.typography.h6)
}