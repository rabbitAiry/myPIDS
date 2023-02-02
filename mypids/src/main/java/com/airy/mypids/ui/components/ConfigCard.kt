package com.airy.mypids.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ConfigurationCard(title: String, content: @Composable () -> Unit) {
    Card(
        Modifier
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
                    .padding(start = 20.dp, top = 12.dp, bottom = 12.dp),
                style = MaterialTheme.typography.h5,
                color = Color.White,
            )
            Surface(modifier = Modifier.padding(10.dp)) {
                content()
            }
        }
    }
}