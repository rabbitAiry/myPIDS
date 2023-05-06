package com.airy.buspids

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.airy.buspids.data.LineTagData
import com.airy.buspids.ui.theme.Black50
import com.airy.buspids.ui.theme.MyPIDSTheme
import com.airy.buspids.vm.WaitingViewModel
import com.airy.pids_lib.ui.golden_200
import com.airy.pids_lib.utils.setFullScreen
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "WaitingActivity"
@AndroidEntryPoint
class WaitingActivity : ComponentActivity() {
    private val vm: WaitingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen()

        setContent {
            MyPIDSTheme {
                var inDebug by remember { mutableStateOf(false) }
                val state by vm.state.collectAsState()
                LaunchedEffect(key1 = state.message) {
                    Toast.makeText(this@WaitingActivity, state.message, Toast.LENGTH_SHORT).show()
                }
                LaunchedEffect(key1 = state.isLineSearchDone) {
                    if(state.isLineSearchDone){
                        startActivity(Intent(this@WaitingActivity, PidsActivity::class.java))
                    }
                }

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(onDoubleTap = { inDebug = !inDebug })
                        },
                    color = MaterialTheme.colors.background
                ) {
                    Greeting(inDebug){ vm.searchLine(it) }
                }
            }
        }
    }

}

@Composable
fun Greeting(inDebug: Boolean, onSelectLine_debug: (LineTagData)->Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(golden_200)
    ) {
        Text(
            text = "欢迎使用公交导视系统",
            Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.h3
        )
        if (inDebug){
            Column(
                Modifier
                    .fillMaxSize()
                    .background(Black50)
                    .padding(30.dp)
            ) {
                Button(onClick = { onSelectLine_debug(LineTagData("e45505067cf66173911fc954", 16, 24)) }) {
                    Text(text = "B8 冼村->学院")
                }
                Button(onClick = { onSelectLine_debug(LineTagData("e45505067cf66173911fc954", 0, 30)) }) {
                    Text(text = "B8 全线 棠德花园方向")
                }
                Button(onClick = {onSelectLine_debug(LineTagData("16de7aa315f7134fc2f1cfaa", 24, 43)) }) {
                    Text(text = "547 猎德大道北->奥体南路总站（优托邦）")
                }
                Button(onClick = { onSelectLine_debug(LineTagData("d23eceaab9eb4d87130daa60", 1, 9)) }) {
                    Text(text = "541 科韵路中->国防大厦")
                }
                Button(onClick = { onSelectLine_debug(LineTagData("ef9e1817b6cba6dc4f2fdf51", 2, 5)) }) {
                    Text(text = "测试（回）")
                }
                Button(onClick = { onSelectLine_debug(LineTagData("ef9e1817b6cba6dc4f2fdf51", 19, 22)) }) {
                    Text(text = "测试（去）")
                }
            }
        }
    }
}