package com.airy.mypids.pids.vertical_style

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator

class MarqueeTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttrInt: Int = 0,
    defStyleRes: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attributeSet) {
    var status = Status.NO_ANIMATION
    private val rect = Rect()
    private var mBaseline = 0f
    private var verticalAnimator : ValueAnimator? = null
    private var horizontalAnimator : ValueAnimator? = null
    private var animateX = 0f
    private var animateY = 0f

    init {
        isSingleLine = true
    }

    override fun onDraw(canvas: Canvas) {
        val n = text.length
        paint.getTextBounds(text.toString(), 0, n, rect)
        mBaseline = if(mBaseline!=0f) mBaseline else baseline.toFloat()
        when(status){
            Status.NO_ANIMATION -> drawAllText(canvas, paddingLeft.toFloat(), mBaseline)
            Status.HORIZONTAL_ANIMATION -> drawAllText(canvas, animateX, mBaseline)
            Status.VERTICAL_ANIMATION -> drawAllText(canvas, paddingLeft.toFloat(), animateY)
        }
    }

    private fun drawAllText(canvas: Canvas, x: Float, y: Float){
        canvas.drawText(text, 0, text.length, x, y, paint)
    }

    fun setText(text: String) {
        super.setText(text, BufferType.NORMAL)
        cancelAnimation()
        val textWidth = paint.measureText(text)
        if(textWidth>width)scrollHorizontal(textWidth)
        else scrollVertical()
    }

    private fun scrollHorizontal(textWidth: Float) {
        status = Status.HORIZONTAL_ANIMATION
        horizontalAnimator = ValueAnimator.ofFloat(width.toFloat(), -textWidth)
        horizontalAnimator?.let{
            it.addUpdateListener { va ->
                animateX = va.animatedValue as Float
                invalidate()
            }
            it.interpolator = LinearInterpolator()
            it.duration = text.length*800L
            it.repeatCount = -1
            it.start()
        }
    }

    private fun scrollVertical() {
        status = Status.VERTICAL_ANIMATION
        verticalAnimator = ValueAnimator.ofFloat(0f, mBaseline)
        verticalAnimator?.let{
            it.addListener(object:AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    status = Status.NO_ANIMATION
                    invalidate()
                }
            })
            it.addUpdateListener { va ->
                animateY = va.animatedValue as Float
                invalidate()
            }
            it.duration = 300
            it.start()
        }
    }

    private fun cancelAnimation(){
        status = Status.NO_ANIMATION
        verticalAnimator?.end()
        horizontalAnimator?.end()
    }

    enum class Status{
        NO_ANIMATION, VERTICAL_ANIMATION, HORIZONTAL_ANIMATION
    }
}