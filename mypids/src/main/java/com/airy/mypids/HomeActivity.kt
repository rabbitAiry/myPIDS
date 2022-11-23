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
import com.airy.mypids.objects.Line
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
    Log.d(TAG, "HomeUI: ${scrollState.value}")
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
                    vm.line,
                    vm.status,
                    onSearch = { cityText, lineText ->
                        vm.onLineSearch(cityText, lineText)
                    },
                    onLineSelected = { info ->
                        vm.onLineSelected(info)
                    },
                    onClearStatus = {
                        vm.line = null
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
                    intent.putExtra("Line", vm.line)
                    intent.putExtra("Style", vm.styleName)
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
fun LineConfiguration(
    resultList: List<PoiInfo>? = null,
    line: Line?,
    status: LineConfigurationStatus,
    onSearch: (String, String) -> Unit,
    onLineSelected: (PoiInfo) -> Unit,
    onClearStatus: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        if (status != LineConfigurationStatus.CHOSEN) {
            LineSearchBar(onSearch)
        }
        if (status == LineConfigurationStatus.IN_CHOOSE) {
            LineResultList(resultList, onLineSelected)
        }
        if (status == LineConfigurationStatus.CHOSEN) {
            SelectedLine(line, onClearStatus)
        }
    }
}

@Composable
fun LineSearchBar(onSearch: (String, String) -> Unit) {
    // TODO: 将测试参数删除
    var cityText by rememberSaveable { mutableStateOf("广州") }
    var lineText by rememberSaveable { mutableStateOf("B4") }

    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(2f),
                value = cityText,
                onValueChange = { cityText = it },
                label = { Text("城市") },
                colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = Color.LightGray),
//                isError = cityText.isEmpty()
            )
            OutlinedTextField(
                modifier = Modifier
                    .weight(5f)
                    .padding(horizontal = 6.dp),
                value = lineText,
                onValueChange = { lineText = it },
                label = { Text("线路名称") },
//                isError = lineText.isEmpty()
            )
            BlackFilledTextButton(
                onClick = {
                    cityText.trim()
                    lineText.trim()
                    onSearch(cityText, lineText)
                },
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .align(Alignment.Bottom)
                    .padding(start = 20.dp)
            ) { Text(text = "搜索") }
        }
        BlackFilledTextButton(onClick = {}) { Text("已保存的自定义线路") }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LineResultList(resultList: List<PoiInfo>?, onLineSelected: (PoiInfo) -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(160.dp)
            .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(6.dp))
            .padding(10.dp)
    ) {
        if (resultList == null) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
            )
        } else {
            if (resultList.isEmpty()) {
                Row(modifier = Modifier.align(Alignment.Center)) {
                    WarningMessage()
                }
            } else {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    items(resultList) { poi ->
                        Surface(onClick = {
                            onLineSelected(poi)
                        }) {
                            Text(text = poi.name, overflow = TextOverflow.Ellipsis, maxLines = 1)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectedLine(line: Line?, onClearStatus: () -> Unit) {
    var dialogShow by remember { mutableStateOf(false) }
    Column(
        Modifier
            .fillMaxWidth()
            .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(6.dp))
            .padding(10.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            if (line == null) {
                WarningMessage()
            } else {
                Text(
                    text = line.lineDescription,
                    style = MaterialTheme.typography.h5
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = onClearStatus
            ) {
                Icon(Icons.Rounded.Clear, "取消选择")
            }
        }
        if (line != null) {
            Row(Modifier.align(Alignment.End)) {
                Text("选择所在站点：", color = Color.DarkGray, modifier = Modifier.alignByBaseline())
                BlackFilledTextButton(modifier = Modifier.alignByBaseline(), onClick = {
                    dialogShow = true
                }) {
                    Text(line.currStation.name, overflow = TextOverflow.Ellipsis, maxLines = 1)
                }
            }
            BlackFilledTextButton(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.End),
                onClick = {
                    // todo
                }) { Text("自定义线路") }
        }
    }
    if (dialogShow) {
        AlertDialog(onDismissRequest = { dialogShow = false }, buttons = {
            LazyColumn(
                Modifier
                    .height(600.dp)
                    .width(320.dp)
                    .padding(10.dp)
            ) {
                line!!.let {
                    items(it.stations) { station ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                            it.setCurrStation(station)
                            dialogShow = false
                        }) {
                            Text(station.name, overflow = TextOverflow.Ellipsis, maxLines = 1)
                        }
                    }
                }
            }
        })
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