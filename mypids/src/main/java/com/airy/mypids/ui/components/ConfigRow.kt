package com.airy.mypids.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * 配置行，通用形式
 * 左侧是配置项标题，右侧提供配置项的填写内容的空间
 */
@Composable
fun ConfigRowOfContent(configTitle: String, content: @Composable RowScope.(Modifier) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(20.dp, 10.dp, 20.dp, 20.dp)
    ) {
        Text(
            configTitle,
            Modifier
                .padding(end = 10.dp)
                .weight(1f, true),
            style = MaterialTheme.typography.subtitle2
        )
        content(Modifier.weight(3f, true))
    }
}

/**
 * 展示一段描述性的文字
 * 只有值[description]不为空时，才展示
 */
@Composable
fun DescriptionText(description: String?) {
    if (description != null) Text(description, style = MaterialTheme.typography.caption)
}

/**
 * 配置行，提供一个TextField
 */
@Composable
fun ConfigRowOfTextField(
    configTitle: String,
    value: String,
    configDescription: String?,
    onValueChange: (String) -> Unit
) {
    ConfigRowOfContent(configTitle = configTitle) {
        Column(it) {
            TextField(value = value, onValueChange = onValueChange)
            DescriptionText(description = configDescription)
        }
    }
}

/**
 * 配置行，提供一组选项
 */
@Composable
fun <T> ConfigRowOfRadioGroup(
    configTitle: String,
    selections: List<T>,
    selectedId: Int,
    configDescription: String?,
    onValueChange: (String) -> Unit
) {
    ConfigRowOfContent(configTitle = configTitle) {
        Column(it) {
            LazyRow {
                itemsIndexed(selections) { index, item ->
                    Row {
                        RadioButton(selected = index == selectedId, onClick = { /*TODO*/ })
                        Text(item.toString(), Modifier.align(Alignment.CenterVertically))
                    }
                }
            }
            DescriptionText(description = configDescription)
        }
    }
}

/**
 * 配置行，提供简易的颜色选择器
 * 由一个用于填写颜色值TextField以及展示颜色值的圆圈组成
 */
@Composable
fun ConfigRowOfColorSelector(
    configTitle: String,
    inputColor: Color,
    inputColorString: String,
    configDescription: String?,
    isWrongColor: Boolean,
    onValueChange: (String) -> Unit
) {
    ConfigRowOfContent(configTitle = configTitle) {
        Row(it){
            Column(Modifier.weight(1f)) {
                TextField(
                    value = inputColorString,
                    onValueChange = onValueChange,
                    isError = isWrongColor
                )
                DescriptionText(description = configDescription)
            }
            Surface(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(30.dp)
                    .align(Alignment.CenterVertically),
                color = inputColor,
                shape = CircleShape,
                border = BorderStroke(2.dp, Color.DarkGray)
            ) {}
        }
    }
}

/**
 * 配置行，提供一个Switch
 */
@Composable
fun ConfigRowOfSwitch(
    configTitle: String,
    isChecked: Boolean,
    configDescription: String?,
    onValueChange: (Boolean) -> Unit
) {
    ConfigRowOfContent(configTitle = configTitle) {
        Column(it) {
            Switch(checked = isChecked, onCheckedChange = onValueChange)
            DescriptionText(description = configDescription)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OfTextField() {
    ConfigRowOfTextField(
        configTitle = "设置线路",
        configDescription = "线路需要慢慢设置才会浪费时间...",
        value = "3"
    ) {}
}

@Preview(showBackground = true)
@Composable
private fun OfRadioGroup() {
    ConfigRowOfRadioGroup(
        configTitle = "选择线路朝向",
        selections = listOf("向北", "向南", "向西", "向东"),
        selectedId = 0,
        configDescription = ""
    ) {}
}

@Preview(showBackground = true)
@Composable
private fun OfColorSelector() {
    ConfigRowOfColorSelector(
        configTitle = "设置线路颜色",
        inputColor = Color.Red,
        inputColorString = "#ff0000",
        configDescription = "choice it!!",
        true
    ) {}
}

@Preview(showBackground = true)
@Composable
private fun OfSwitch() {
    ConfigRowOfSwitch(
        configTitle = "启用颜色",
        isChecked = true,
        configDescription = ""
    ) {}
}