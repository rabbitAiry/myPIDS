package com.airy.driver_pids

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airy.pids_lib.ui.components.ConfigRowOfTextField
import com.airy.pids_lib.ui.components.ScalableTopBar
import com.airy.driver_pids.ui.theme.MyPIDSTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPIDSTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val scrollState = rememberScrollState()
                    var credit by remember { mutableStateOf("") }
                    var key by remember { mutableStateOf("") }

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
                                value = credit,
                                onValueChange = { credit = it }
                            )
                            ConfigRowOfTextField(
                                configTitle = "密码",
                                value = key,
                                onValueChange = { key = it })
                            Spacer(modifier = Modifier.weight(1f))
                            Button(onClick = {
                                if (key == "123"){
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            ControlActivity::class.java
                                        )
                                    )
                                } else {
                                    Toast.makeText(this@LoginActivity, "wrong password", Toast.LENGTH_SHORT).show()
                                }
                            }) {
                                Text(text = "确定")
                            }
                        }
                    }
                }
            }
        }
    }
}
