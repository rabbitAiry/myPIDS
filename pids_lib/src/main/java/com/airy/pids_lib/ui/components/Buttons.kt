package com.airy.pids_lib.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun ColumnScope.NextStepButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    StepButton(enabled = enabled, text = "下一步", onClick)
}

@Composable
fun ColumnScope.FinishStepsButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    StepButton(enabled = enabled, text = "大功告成", onClick)
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

@Composable
fun BlackFilledTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick,
        modifier,
        enabled,
        interactionSource,
        elevation,
        shape,
        border,
        ButtonDefaults.buttonColors(Color.Black, Color.White),
        contentPadding,
        content
    )
}

