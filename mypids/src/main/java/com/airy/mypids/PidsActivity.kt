package com.airy.mypids

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airy.mypids.databinding.ActivityPidsBinding
import com.airy.mypids.objects.LineInfo
import com.airy.mypids.pids.BasePidsFragment
import com.airy.mypids.pids.PidsManager
import com.airy.mypids.pids.BasePidsFragment.PidsStatus
import com.airy.mypids.views.HelperLayout
import java.lang.Exception

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

        try {
//            intent!!.let{
//                val lineInfo = it.getParcelableExtra<LineInfo>("Line")!!
//                val styleText = it.getStringExtra("Style")!!
//                setFullScreen(PidsManager.getIsHorizontal(styleText)!!)
//                pidsFragment = PidsManager.getPidsFragment(styleText, this, lineInfo)!!
//                setPidsFragment()
//                setPidsHelperWindow()
//            }
        }catch (e: Exception){
            Toast.makeText(this, "打开时出错", Toast.LENGTH_SHORT).show()
        }
        binding.pidsProgressBar.visibility = View.INVISIBLE
    }

    private fun setPidsFragment(){
        supportFragmentManager.beginTransaction().add(binding.root.id, pidsFragment).commit()
    }

    /**
     * app内悬浮窗
     */
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
        val layout: HelperLayout = windowView.findViewById(R.id.window_layout)
        layout.setWindowParams(params, manager, windowView)
        manager.addView(windowView, params)
    }

    @Suppress("DEPRECATION")
    private fun setFullScreen(landscape : Boolean){
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        if(landscape)requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;//强制为横屏
    }

    private fun nextStation(){
        pidsFragment.nextStation()
    }

    private fun stationArrived(){
        if(pidsFragment.status == PidsStatus.BUS_STATION_ARRIVED)return
        pidsFragment.pidsStationArrived(true)
    }

    private fun busRun(){
        if(pidsFragment.status in arrayOf(PidsStatus.BUS_RUNNING, PidsStatus.BUS_RUNNING_START, PidsStatus.BUS_RUNNING_REPORTING))return
        nextStation()
        pidsFragment.pidsRunning()
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(windowView)
    }
}