package com.airy.mypids.ui.home

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airy.mypids.DisplayActivity
import com.airy.mypids.pids.PidsManager
import com.airy.mypids.ui.components.ConfigRowOfRadioGroup
import com.airy.mypids.ui.components.RowWarningText
import com.airy.mypids.ui.components.TitleCard
import com.airy.mypids.data.PidsData

@Composable
fun MainScreen(pids: PidsData, scrollState: ScrollState, context: Context, onLineButtonClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .verticalScroll(scrollState)
    ) {
        TitleCard(title = "线路") {
            LineCard(pids, onLineButtonClick)
        }
        TitleCard(title = "pids风格") {
            StyleCard(pids)
        }
        StartPidsButton(
            context,
            pids.checkLineNotNull(),
            pids,
            Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .height(60.dp)
        )
    }
}

@Composable
private fun ColumnScope.LineCard(
    pids: PidsData,
    onLineButtonClick: () -> Unit
) {
    if (pids.lineInfo?.lineName != null) {
        Text(text = pids.lineInfo!!.lineName)
    } else {
        Row {
            RowWarningText(text = "未选择线路")
            Button(onClick = onLineButtonClick) {
                Text(text = "配置线路")
            }
        }
    }
}

@Composable
private fun ColumnScope.StyleCard(pids: PidsData) {
    ConfigRowOfRadioGroup(
        configTitle = "选择pids风格",
        selections = PidsManager.pidsNameList,
        selected = pids.style,
        configDescription = null,
        onValueChange = { pids.style = it }
    )
}

@Composable
fun StartPidsButton(context: Context, isReady: Boolean, pids: PidsData, modifier: Modifier = Modifier) {
    Button(
        enabled = isReady, onClick = {
//            val target = if(PidsManager.getIsHorizontal(pids.style)) PidsLandscapeActivity::class.java else PidsActivity::class.java
            val intent = Intent(context, DisplayActivity::class.java)
            context.startActivity(intent)
        }, modifier = modifier
    ) {
        Text("启动PIDS")
    }
}
