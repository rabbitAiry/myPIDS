package com.airy.mypids.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * 使用该View把用户的触摸输入时间消费了
 * 使得在该View之下的view不会发生触摸事件
 */
class ConsumeTouchView : View {
    constructor(context: Context): super(context)
    @JvmOverloads constructor(context: Context,
                attributeSet: AttributeSet?,
                defStyleAttr: Int = 0,
                defStyleRes: Int = 0):super(context, attributeSet, defStyleAttr, defStyleRes)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean = true
}