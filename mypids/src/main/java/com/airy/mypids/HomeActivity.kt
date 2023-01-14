package com.airy.mypids

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airy.mypids.ui.theme.MyPIDSTheme
import com.baidu.mapapi.search.core.PoiInfo
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airy.mypids.objects.LineInfo
import com.airy.mypids.utils.UnitUtil

private const val TAG = "HomeActivity"

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPIDSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomeUI()
                }
            }
        }
    }
}

@Composable
fun HomeUI(vm: HomeViewModel = viewModel()) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            val originHeight = 160
            val minHeight = 60
            val loss =
                Math.min(originHeight - minHeight, UnitUtil.px2dp(context, scrollState.value))
            val height = originHeight - loss
            val textStyle =
                if (height == minHeight) MaterialTheme.typography.h5 else MaterialTheme.typography.h4
            val bottomPadding = if (height == minHeight) 10 else 20
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height.dp)
                    .background(MaterialTheme.colors.primary)
            ) {
                Text(
                    text = "配置我的pids",
                    style = textStyle,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 20.dp, bottom = bottomPadding.dp)
                )
            }
        }
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 42.dp)
                .verticalScroll(scrollState)
        ) {
            ConfigurationCard(title = "线路") {
                LineConfiguration(
                    vm.resultList,
                    vm.lineInfo,
                    vm.status,
                    onSearch = { cityText, lineText ->
                        vm.onLineSearch(cityText, lineText)
                    },
                    onLineSelected = { info ->
                        vm.onLineSelected(info)
                    },
                    onClearStatus = {
                        vm.lineInfo = null
                        vm.status = LineConfigurationStatus.NOT_CHOOSE
                    }
                )
            }
            ConfigurationCard(title = "pids风格") {
                PidsStyleConfiguration(
                    vm.styleName,
                    vm.pidsOptionList
                ) {
                    vm.styleName = it
                }
            }
            Button(
                enabled = vm.isPidsReady(),
                onClick = {
                    val intent = Intent(context, PidsActivity::class.java)
//                    intent.putExtra("Line", vm.lineInfo)
//                    intent.putExtra("Style", vm.styleName)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text("启动PIDS")
            }
        }
    }
}

@Composable
fun WarningMessage() {
    Icon(Icons.Rounded.Warning, "注意")
    Text("找不到对应线路信息", color = Color.DarkGray)
}

@Composable
fun PidsStyleConfiguration(
    styleName: String,
    pidsOptionList: List<String>,
    onStyleChange: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .selectableGroup()
            .fillMaxWidth()
    ) {
        items(pidsOptionList) { name ->
            Surface(
                modifier = Modifier
                    .selectable(
                        selected = (name == styleName),
                        onClick = { onStyleChange(name) },
                        role = Role.RadioButton
                    ), shape = RoundedCornerShape(6.dp), border = BorderStroke(2.dp, Color.DarkGray)
            ) {
                Row(Modifier.padding(10.dp)) {
                    RadioButton(selected = (name == styleName), onClick = null)
                    Text(text = name)
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

@Composable
fun ExtraConfiguration() {
    val switchPidsOptions = listOf("定位", "悬浮窗", "通知栏")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(switchPidsOptions[0]) }
    Column(Modifier.fillMaxWidth()) {
        Row {
            Text("切换报站方式")
            switchPidsOptions.forEach {
                Row(
                    Modifier
                        .padding(10.dp)
                        .selectable(
                            selected = it == selectedOption,
                            onClick = { onOptionSelected(it) },
                            role = Role.Checkbox
                        )
                ) {
                    Checkbox(checked = it == "悬浮窗", onCheckedChange = null)
                    Text(it)
                }
            }
        }
        Text("启用英语翻译")
        Text("启用报站")
        Text("启用播报内容")
        Text("选择线路颜色")
    }
}

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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyPIDSTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            HomeUI()
        }
    }
}