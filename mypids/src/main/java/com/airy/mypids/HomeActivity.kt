package com.airy.mypids

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airy.mypids.ui.theme.MyPIDSTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.airy.mypids.ui.components.TitleCard
import com.airy.mypids.ui.components.RowWarningText
import com.airy.mypids.ui.components.ScalableTopBar
import com.airy.mypids.ui.home.LineConfigScreen
import com.airy.mypids.ui.home.LineScreen

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
fun HomeUI() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val navController = rememberNavController()
    val currBackStack by navController.currentBackStackEntryAsState()
    val currTitle = currBackStack?.destination?.route
    val currDestination = destinationsList.find { it.route == currTitle } ?: Start
    Scaffold(
        topBar = {
            ScalableTopBar(
                title = currDestination.title,
                scrollState = scrollState,
                context = context
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Start.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Start.route) {
                MainScreen(scrollState = scrollState, context = context){
                    navController.navigate(Line.route){
                        restoreState = true
                    }
                }
            }
            composable(route = Line.route) {
                LineScreen()
            }
            composable(route = LineConfig.route) {
                LineConfigScreen()
            }
        }

    }
}

@Composable
fun MainScreen(scrollState: ScrollState, context: Context, onLineButtonClick: ()->Unit) {
    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 42.dp)
            .verticalScroll(scrollState)
    ) {
        TitleCard(title = "线路") {
            var lineConsists by remember { mutableStateOf(false) }
            if (lineConsists){
                Text(text = "")
            }else{
                Row {
                    RowWarningText(text = "未选择线路")
                    Button(onClick = onLineButtonClick) {
                        Text(text = "配置线路")
                    }
                }
            }
        }
        TitleCard(title = "pids风格") {
            Text("normal")
        }
        StartPidsButton(
            context,
            false, // TODO
            Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .height(60.dp)
        )
    }
}

@Composable
fun StartPidsButton(context: Context, isReady: Boolean, modifier: Modifier = Modifier) {
    Button(
        enabled = isReady,
        onClick = {
            val intent = Intent(context, PidsActivity::class.java)
//            context.startActivity(intent)
        },
        modifier = modifier
    ) {
        Text("启动PIDS")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyPIDSTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            HomeUI()
        }
    }
}