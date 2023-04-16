package com.airy.passengers_pids

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.airy.passengers_pids.ui.BusWaitingScreen
import com.airy.passengers_pids.ui.LineSelectionScreen
import com.airy.passengers_pids.ui.OnJourneyScreen
import com.airy.passengers_pids.ui.TerminalSelectionScreen
import com.airy.passengers_pids.ui.theme.MyPIDSTheme
import com.airy.pids_lib.ui.components.ScalableTopBar

class PassengerActivity : ComponentActivity() {
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
                    val navController = rememberNavController()
                    val currBackStack by navController.currentBackStackEntryAsState()
                    val currTitle = currBackStack?.destination?.route
                    val currDestination =
                        destinationsList.find { it.route == currTitle } ?: LineSelection

                    Scaffold(topBar = {
                        ScalableTopBar(
                            title = currDestination.title,
                            isMain = currDestination == OnJourney,
                            scrollState = scrollState,
                            context = applicationContext
                        )
                    }) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = LineSelection.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(route = LineSelection.route){
                                LineSelectionScreen()
                            }
                            composable(route = TerminalSelection.route){
                                TerminalSelectionScreen()
                            }
                            composable(route = BusWaiting.route){
                                BusWaitingScreen()
                            }
                            composable(route = OnJourney.route){
                                OnJourneyScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}

