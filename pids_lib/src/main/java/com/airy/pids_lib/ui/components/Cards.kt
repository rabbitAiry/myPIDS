package com.airy.pids_lib.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TitleCard(title: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp), shape = RoundedCornerShape(10.dp),
        elevation = 10.dp
    ) {
        Column {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                style = MaterialTheme.typography.h5,
                color = Color.White,
            )
            Surface(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                content()
            }
        }
    }
}

@Composable
fun ContentCard(strokeColor: Color = Color.DarkGray, maxHeight: Int = 240, content: @Composable () -> Unit){
    Card(
        modifier = Modifier
            .padding(20.dp)
            .heightIn(min = 100.dp, max = maxHeight.dp)
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, strokeColor),
    ) {
        content()
    }
}