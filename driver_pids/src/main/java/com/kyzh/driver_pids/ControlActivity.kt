package com.kyzh.driver_pids

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
import com.kyzh.driver_pids.ui.theme.MyPIDSTheme

class ControlActivity : ComponentActivity() {
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

                    Scaffold(topBar = {
                        ScalableTopBar(
                            title = "选择线路",
                            scrollState = scrollState,
                            context = applicationContext
                        )
                    }) { innerPadding ->
                        Column(Modifier.padding(innerPadding)) {

                        }
                    }
                }
            }
        }
    }
}