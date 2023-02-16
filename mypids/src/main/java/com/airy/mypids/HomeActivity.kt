package com.airy.mypids

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.airy.mypids.ui.components.ScalableTopBar
import com.airy.mypids.ui.home.LineConfigScreen
import com.airy.mypids.ui.home.LineScreen
import com.airy.mypids.ui.home.MainScreen
import com.airy.mypids.ui.theme.MyPIDSTheme
import com.airy.mypids.viewmodels.PidsData


private const val TAG = "HomeActivity"

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPIDSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    HomeUI()
                }
            }
        }
        setTransparentTopBar()
    }

    private fun setTransparentTopBar() {
        val decorView = window.decorView
        val option =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        decorView.systemUiVisibility = option
        window.statusBarColor = Color.TRANSPARENT
    }
}

@Composable
fun HomeUI() {
    val pids = PidsData
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val navController = rememberNavController()
    val currBackStack by navController.currentBackStackEntryAsState()
    val currTitle = currBackStack?.destination?.route
    val currDestination = destinationsList.find { it.route == currTitle } ?: Start
    val isMain = currDestination == Start
    Scaffold(topBar = {
        ScalableTopBar(
            title = currDestination.title,
            isMain = isMain,
            scrollState = scrollState,
            context = context
        )
    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Start.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Start.route) {
                MainScreen(pids, scrollState = scrollState, context = context) {
                    navController.navigate(Line.route) {
                        restoreState = true
                    }
                }
            }
            composable(route = Line.route) {
                LineScreen(pids, navController)
            }
            composable(route = LineConfig.route) {
                LineConfigScreen(pids, navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyPIDSTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
        ) {
            HomeUI()
        }
    }
}