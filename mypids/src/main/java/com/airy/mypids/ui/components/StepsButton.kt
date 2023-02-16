package com.airy.mypids.ui.components

import android.content.res.Resources.Theme
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airy.mypids.Start

@Composable
fun ColumnScope.NextStepButton(
    enabled: Boolean,
    navController: NavHostController,
    route: String
) {
    StepButton(enabled = enabled, text = "下一步") {
        navController.navigate(route) {
            restoreState = true
        }
    }
}

@Composable
fun ColumnScope.FinishStepsButton(
    enabled: Boolean,
    navController: NavHostController
) {
    StepButton(enabled = enabled, text = "大功告成") {
        navController.popBackStack(Start.route, false)
    }
}

@Composable
fun ColumnScope.StepButton(
    enabled: Boolean,
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .align(Alignment.End)
            .padding(40.dp),
        enabled = enabled,
        onClick = onClick,
        content = { Text(text, style = MaterialTheme.typography.button) }
    )
}