package com.airy.buspids

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.airy.buspids.ui.PidsUI
import com.airy.buspids.ui.theme.MyPIDSTheme
import com.airy.buspids.ui.views.PidsControllerWindowView
import com.airy.buspids.vm.PidsViewModel
import com.airy.pids_lib.fake_data.lineInfoB8
import com.airy.pids_lib.utils.setFullScreen

class PidsActivity : ComponentActivity() {
    lateinit var vm: PidsViewModel
    private lateinit var windowView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen()

        vm = ViewModelProvider(this)[PidsViewModel::class.java]
        setPidsHelperWindow()
        setContent {
            MyPIDSTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    PidsUI(vm.line, vm.postList)
                }
            }
        }
    }

    /**
     * app内悬浮窗
     */
    private fun setPidsHelperWindow() {
        windowView = layoutInflater.inflate(R.layout.window_pids_controller, null)
        val buttonNextStation: Button = windowView.findViewById(R.id.button_next_station)
        val buttonArrive: Button = windowView.findViewById(R.id.button_arrive)
        val buttonDrive: Button = windowView.findViewById(R.id.button_drive)
        buttonNextStation.setOnClickListener { vm.line.goNextStation() }
        buttonArrive.setOnClickListener { vm.line.stationArrived() }
        buttonDrive.setOnClickListener { vm.line.busRun() }

        val params = WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        }
        val layout: PidsControllerWindowView = windowView.findViewById(R.id.window_layout)
        layout.setWindowParams(params, windowManager, windowView)
        windowManager.addView(windowView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(windowView)
    }
}