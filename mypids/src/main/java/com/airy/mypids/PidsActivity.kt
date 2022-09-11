package com.airy.mypids

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.airy.mypids.databinding.ActivityPidsBinding
import com.airy.mypids.objects.Line
import com.airy.mypids.pids.BasePidsFragment
import com.airy.mypids.pids.gz_bus_style.GZBusStyleFragment
import com.airy.mypids.pids.vertical_style.VerticalPidsFragment
import com.airy.mypids.views.WindowLayout

class PidsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPidsBinding
    private lateinit var pidsFragment: BasePidsFragment
    private lateinit var windowView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPidsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.pidsProgressBar.visibility = View.VISIBLE

        intent?.let{
            val line = it.getParcelableExtra<Line>("Line")!!
            setFullScreen(false)
            pidsFragment = VerticalPidsFragment(this, line)
//            pidsFragment = GZBusStyleFragment(this, line)
            setPidsFragment()
            setPidsHelperWindow()
        }

        binding.pidsProgressBar.visibility = View.INVISIBLE
    }

    private fun setPidsFragment(){
        supportFragmentManager.beginTransaction().add(binding.root.id, pidsFragment).commit()
    }

    private fun setPidsHelperWindow(){
        windowView = layoutInflater.inflate(R.layout.window_helper, null)
        val buttonNextStation: Button = windowView.findViewById(R.id.button_next_station)
        val buttonArrive : Button = windowView.findViewById(R.id.button_arrive)
        val buttonDrive: Button = windowView.findViewById(R.id.button_drive)
        buttonNextStation.setOnClickListener { nextStation() }
        buttonArrive.setOnClickListener { stationArrived() }
        buttonDrive.setOnClickListener { busRun() }

        val params = WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        }
        val manager = windowManager
        val layout: WindowLayout = windowView.findViewById(R.id.window_layout)
        layout.setWindowParams(params, manager, windowView)
        manager.addView(windowView, params)
    }

    private fun setFullScreen(landscape : Boolean){
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        if(landscape)requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;//强制为横屏
    }

    private fun nextStation(){
        pidsFragment.nextStation()
    }

    private fun stationArrived(){
        pidsFragment.pidsStationArrived()
    }

    private fun busRun(){
        pidsFragment.pidsRunning()
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(windowView)
    }
}