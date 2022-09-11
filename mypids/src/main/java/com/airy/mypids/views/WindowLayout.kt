package com.airy.mypids.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import kotlin.math.pow

class WindowLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    private var x = 0
    private var y = 0
    private lateinit var params: WindowManager.LayoutParams
    private lateinit var windowManager: WindowManager
    private lateinit var root: View

    fun setWindowParams(params: WindowManager.LayoutParams, windowManager: WindowManager, root: View){
        this.params = params
        this.windowManager = windowManager
        this.root = root
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var intercepted = false
        ev?.let{
            when(it.action){
                MotionEvent.ACTION_DOWN ->{
                    x = it.rawX.toInt()
                    y = it.rawY.toInt()
                }
                MotionEvent.ACTION_MOVE ->{
                    val nx = it.rawX.toInt()
                    val ny = it.rawY.toInt()
                    intercepted = (nx - x).toDouble().pow(2.0) + (ny - y).toDouble().pow(2.0) >=100
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
                else -> return false
            }
        }
        return true
    }
}