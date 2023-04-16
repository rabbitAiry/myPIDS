package com.airy.buspids

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.airy.buspids.ui.theme.MyPIDSTheme
import com.airy.buspids.vm.WaitingViewModel
import com.airy.pids_lib.ui.golden_200

class WaitingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: 观察该值
        val vm = ViewModelProvider(this)[WaitingViewModel::class.java]
        if(vm.prepareded){
            // TODO 临时措施，防止无法退出
            vm.prepareded = false
            startActivity(Intent(this, PidsActivity::class.java))
        }

        setContent {
            MyPIDSTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
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
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyPIDSTheme {
        Greeting()
    }
}