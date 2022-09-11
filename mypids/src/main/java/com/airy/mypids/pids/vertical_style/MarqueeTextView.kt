package com.airy.mypids.pids.vertical_style

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.text.TextUtils
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.TextView

class MarqueeTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttrInt: Int = 0,
    defStyleRes: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attributeSet) {
    var isMarquee = false

    init {
        isSingleLine = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun isFocused(): Boolean = isMarquee

    fun setText(text: String) {
        super.setText(text, BufferType.NORMAL)
        post {
            if(isMarquee)endMarquee()
            val textWidth = paint.measureText(text)
            if(textWidth > width)startMarquee()
            else loadTextAnimate(text)
        }
    }

    private fun startMarquee(){
        isMarquee = true
        postDelayed({
            if(!isMarquee)return@postDelayed
            ellipsize = TextUtils.TruncateAt.MARQUEE
            isFocusable = true
            isFocusableInTouchMode = true
            marqueeRepeatLimit = -1
        }, 3000)
    }

    private fun endMarquee(){
        isMarquee = false
        ellipsize = TextUtils.TruncateAt.END
    }

    private fun loadTextAnimate(text: String){
        val animator = ValueAnimator.ofInt(height, paddingBottom)
        animator.addUpdateListener {
            val temp = it.animatedValue as Int
//            paddingBottom = temp
        }
    }
}