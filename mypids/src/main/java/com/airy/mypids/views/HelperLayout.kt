package com.airy.mypids.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Space
import kotlin.math.pow

class HelperLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    private var x = 0
    private var y = 0
    private var phoneHeight = 0
    private var phoneWeight = 0
    private var isFolded = false
    private lateinit var params: WindowManager.LayoutParams
    private lateinit var windowManager: WindowManager
    private lateinit var root: View
    private lateinit var spaceView: Space
    private var vh = 0
    private var vw = 0

    @Suppress("DEPRECATION")
    fun setWindowParams(params: WindowManager.LayoutParams, windowManager: WindowManager, root: View){
        this.params = params
        this.windowManager = windowManager
        this.root = root
        this.phoneHeight = windowManager.defaultDisplay.height
        this.phoneWeight = windowManager.defaultDisplay.width
        post {
            vh = root.height
            vw = root.width
            spaceView = Space(root.context)
            spaceView.layoutParams = LayoutParams(0, vh)
            addView(spaceView, 0)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var intercepted = false
        if(isFolded)return true
        ev?.let{
            when(it.action){
                MotionEvent.ACTION_DOWN ->{
                    x = it.rawX.toInt()
                    y = it.rawY.toInt()
                }
                MotionEvent.ACTION_MOVE ->{
                    val nx = it.rawX.toInt()
                    val ny = it.rawY.toInt()
                    if((nx - x).toDouble().pow(2.0) + (ny - y).toDouble().pow(2.0)>50){
                        // 防止消耗按钮点击事件
                        intercepted = true
                    }
                }
            }
        }
        return intercepted
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let{
            when(it.action){
                MotionEvent.ACTION_DOWN ->{
                    x = it.rawX.toInt()
                    y = it.rawY.toInt()
                }
                MotionEvent.ACTION_MOVE->{
                    val nx = it.rawX.toInt()
                    val ny = it.rawY.toInt()
                    val moveX = nx-x
                    val moveY = ny-y
                    params.apply {
                        x += moveX
                        y += moveY
                    }
                    x = nx
                    y = ny
                    windowManager.updateViewLayout(root, params)
                }
                MotionEvent.ACTION_UP->{
                    if(x-vw/4<0)fold(Side.LEFT)
                    else if(x+vw/4>phoneWeight)fold(Side.RIGHT)
                    else if(y-vh/4<0)fold(Side.TOP)
                    else if(y+vh/4>phoneHeight)fold(Side.BOTTOM)
                    else unfold()
                }
                else -> return true
            }
        }
        return true
    }

    private fun fold(side: Side){
        if(isFolded)return
        isFolded = true
        params.apply {
            width = vh
            height = vh
            setBackgroundColor(Color.DKGRAY)
        }
        spaceView.layoutParams = LayoutParams(vh, vh)
        windowManager.updateViewLayout(root, params)
    }

    private fun unfold(){
        if(!isFolded)return
        isFolded = false
        params.apply {
            width = vw
            setBackgroundColor(Color.BLACK)
        }
        spaceView.layoutParams = LayoutParams(0, vh)
        windowManager.updateViewLayout(root, params)
    }

    enum class Side{
        LEFT, RIGHT, TOP, BOTTOM
    }
}