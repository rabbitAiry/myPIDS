package com.airy.mypids.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

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

@Composable
fun WarningMessage(text: String, modifier: Modifier = Modifier) {
    Box(modifier) {
        Icon(Icons.Rounded.Warning, "注意")
        Text(text, color = Color.DarkGray)
    }
}

@Composable
fun ColorSelector(labelText: String, colorText: String, onColorTextChange: (String) -> Unit) {
    var color = Color.White
    var colorError = false
    try {
        color = Color(colorText.toInt(16))
    }catch (e:Exception){
        colorError = true
    }
    Row {
        OutlinedTextField(
            value = colorText,
            onValueChange = onColorTextChange,
            label = { Text(text = labelText) },
            isError = colorError
        )
        Surface(modifier = Modifier.width(24.dp).height(24.dp), shape = CircleShape, color = color, elevation = 10.dp) {
        }
    }
}