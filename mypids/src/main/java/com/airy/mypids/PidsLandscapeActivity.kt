package com.airy.mypids

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import com.airy.mypids.databinding.ActivityPidsBinding
import com.airy.mypids.pids.BasePidsFragment
import com.airy.mypids.pids.PidsManager
import com.airy.mypids.viewmodels.PidsData
import com.airy.mypids.views.HelperLayout

private const val TAG = "PidsLandscapeActivity"
class PidsLandscapeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPidsBinding
    private lateinit var pidsFragment: BasePidsFragment
    private lateinit var windowView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        binding = ActivityPidsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFullScreen()
        supportActionBar?.hide()
        binding.pidsProgressBar.visibility = View.VISIBLE
        val styleText = PidsData.style
        pidsFragment = PidsManager.getPidsFragment(styleText, this, PidsData.lineInfo!!, PidsData.stationListInfo!!)!!
        setPidsFragment()
        setPidsHelperWindow()
        binding.pidsProgressBar.visibility = View.INVISIBLE
        Log.e(TAG, "onCreate: ", )
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "onStart: ", )
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume: ", )
    }

    private fun setPidsFragment() {
        supportFragmentManager.beginTransaction().add(binding.root.id, pidsFragment).commit()
    }

    /**
     * app内悬浮窗
     */
    private fun setPidsHelperWindow() {
        windowView = layoutInflater.inflate(R.layout.window_helper, null)
        val buttonNextStation: Button = windowView.findViewById(R.id.button_next_station)
        val buttonArrive: Button = windowView.findViewById(R.id.button_arrive)
        val buttonDrive: Button = windowView.findViewById(R.id.button_drive)
        buttonNextStation.setOnClickListener { nextStation() }
        buttonArrive.setOnClickListener { stationArrived() }
        buttonDrive.setOnClickListener { busRun() }

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

    @Suppress("DEPRECATION")
    private fun setFullScreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun nextStation() {
        pidsFragment.nextStation()
    }

    private fun stationArrived() {
        if (pidsFragment.status == BasePidsFragment.PidsStatus.BUS_STATION_ARRIVED) return
        pidsFragment.pidsStationArrived(true)
    }

    private fun busRun() {
        if (pidsFragment.status in arrayOf(
                BasePidsFragment.PidsStatus.BUS_RUNNING,
                BasePidsFragment.PidsStatus.BUS_RUNNING_START,
                BasePidsFragment.PidsStatus.BUS_RUNNING_REPORTING
            )
        ) return
        nextStation()
        pidsFragment.pidsRunning()
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(windowView)
    }
}