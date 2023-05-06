package com.airy.driver_pids

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.airy.pids_lib.ui.components.ConfigRowOfTextField
import com.airy.pids_lib.ui.components.ScalableTopBar
import com.airy.driver_pids.ui.theme.MyPIDSTheme
import com.airy.driver_pids.viewmodel.LoginViewModel
import com.airy.pids_lib.ui.Black50
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPIDSTheme {
                val scrollState = rememberScrollState()
                var driverName by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }
                var host by remember { mutableStateOf("192.168.5.6") }
                var inDebug by remember { mutableStateOf(false) }

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(onDoubleTap = { inDebug = !inDebug })
                        },
                    color = MaterialTheme.colors.background
                ) {
                    LaunchedEffect(key1 = viewModel.isLogin) {
                        if (viewModel.isLogin) {
                            startActivity(
                                Intent(
                                    this@LoginActivity,
                                    ControlActivity::class.java
                                )
                            )
                            finish()
                        }
                    }

                    LaunchedEffect(key1 = viewModel.isErrorLogin) {
                        if (viewModel.isErrorLogin) {
                            Toast.makeText(this@LoginActivity, "密码错误", Toast.LENGTH_SHORT).show()
                        }
                    }

                    Box(){
                        Scaffold(topBar = {
                            ScalableTopBar(
                                title = "首次登录",
                                scrollState = scrollState,
                                context = applicationContext
                            )
                        }) { innerPadding ->
                            Column(Modifier.padding(innerPadding)) {
                                ConfigRowOfTextField(
                                    configTitle = "账号",
                                    value = driverName,
                                    onValueChange = { driverName = it }
                                )
                                ConfigRowOfTextField(
                                    configTitle = "密码",
                                    value = password,
                                    onValueChange = { password = it })
                                Spacer(modifier = Modifier.weight(1f))
                                Button(modifier = Modifier.padding(40.dp), onClick = {
                                    viewModel.login(driverName, password, host)
                                }) {
                                    Text(text = "确定")
                                }
                            }
                        }
                        if (inDebug){
                            Row(
                                Modifier
                                    .fillMaxSize()
                                    .background(Black50)
                                    .padding(30.dp)
                            ) {
                                ConfigRowOfTextField(
                                    configTitle = "Host",
                                    value = host,
                                    onValueChange = { host = it })

                                Button(onClick = { viewModel.isLogin = true }) {
                                    Text("强制登录")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
