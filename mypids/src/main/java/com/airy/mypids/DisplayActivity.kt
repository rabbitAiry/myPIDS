package com.airy.mypids

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.airy.mypids.pids.PidsManager
import com.airy.mypids.data.PidsData
import com.airy.mypids.pids.vertical_style.VerticalPidsScreen
import com.airy.mypids.ui.theme.MyPIDSTheme
import com.airy.mypids.viewmodels.DisplayViewModel
import com.airy.mypids.views.HelperLayout

class DisplayActivity : AppCompatActivity() {
    private lateinit var windowView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val displayModel = ViewModelProvider(this)[DisplayViewModel::class.java]
//        val styleText = PidsData.style
//        if (PidsManager.getIsHorizontal(styleText)) requestedOrientation =
//            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;//强制为横屏
        setFullScreen()

        setContent {
            MyPIDSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    DisplayUi(displayModel)
                }
            }
        }
        setPidsHelperWindow(displayModel)
    }

    /**
     * app内悬浮窗
     */
    private fun setPidsHelperWindow(vm: DisplayViewModel) {
        windowView = layoutInflater.inflate(R.layout.window_helper, null)
        val buttonNextStation: Button = windowView.findViewById(R.id.button_next_station)
        val buttonArrive: Button = windowView.findViewById(R.id.button_arrive)
        val buttonDrive: Button = windowView.findViewById(R.id.button_drive)
        buttonNextStation.setOnClickListener { vm.nextStation() }
        buttonArrive.setOnClickListener { vm.stationArrived() }
        buttonDrive.setOnClickListener { vm.busRun() }

        val params = WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        }
        val manager = windowManager
        val layout: HelperLayout = windowView.findViewById(R.id.window_layout)
        layout.setWindowParams(params, manager, windowView)
        manager.addView(windowView, params)
    }

    private fun setFullScreen() {
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(windowView)
    }
}

@Composable
fun DisplayUi(vm: DisplayViewModel) {
    VerticalPidsScreen(vm)
}