package com.airy.mypids.pids.gz_bus_style
//
//import android.animation.Animator
//import android.animation.AnimatorListenerAdapter
//import android.animation.ValueAnimator
//import android.annotation.SuppressLint
//import android.content.Context
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.Paint
//import android.util.AttributeSet
//import android.view.View
//import com.airy.mypids.R
//import com.airy.mypids.utils.ResUtil
//
//class FlowingText @JvmOverloads constructor(
//    context: Context,
//    attributeSet: AttributeSet? = null,
//    defStyleAttr: Int = 0,
//    private val isFirst: Boolean = false,
//    private val isEnd: Boolean = false,
//    private val text: String = "",
//    status: Status = Status.ARRIVED
//) : View(context, attributeSet, defStyleAttr) {
//    private val backgroundPaint = Paint()
//    private val linePaint = Paint()
//    private val circlePaint = Paint()
//    private val textPaint = Paint()
//    private val gray500 = ResUtil.getResColor(R.color.gray_500, context)
//    private val blue500 = ResUtil.getResColor(R.color.light_blue_500, context)
//    private val blue800 = ResUtil.getResColor(R.color.light_blue_800, context)
//    private val red500 = ResUtil.getResColor(R.color.red_500, context)
//    private var textHead = 0f
//    private var textEnd = 0f
//    private var textY = 0f
//    private var statusAnimation =
//        ValueAnimator.ofFloat()   // 待修复：statusAnimation仍然会在该view不可见的情况下继续进行动画
//    private var textAnimation = ValueAnimator.ofFloat()
//    private var textInAnimate = false
//    var status = Status.ARRIVED
//        set(value) {
//            field = value
//            statusAnimation.cancel()
//            circlePaint.color = if (status == Status.NOT_ARRIVE) blue800 else gray500
//            textPaint.color = when (status) {
//                Status.NOT_ARRIVE -> blue500
//                Status.CURR -> Color.WHITE
//                Status.ARRIVED -> gray500
//            }
//            if (status == Status.CURR) statusAnimate()
//            else invalidate()
//        }
//
//    init {
//        linePaint.color = gray500
//        this.status = status
//    }
//
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        textAnimation.cancel()
//        textInAnimate = false
//    }
//
//    @SuppressLint("DrawAllocation")
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//        backgroundPaint.color = if (status == Status.CURR) blue500 else Color.WHITE
//        canvas.drawColor(if (status == Status.CURR) blue500 else Color.WHITE)
//
//        val radius = width / 8f * 3
//        val center = width / 2f
//        val parent = parent as LineMapLayout
//        val textSize = parent.textSize
//        textPaint.textSize = textSize
//        textHead = center * 2 + textSize
//        if (!textInAnimate) textY = textHead
//        textEnd = drawText(width / 8f, textY, canvas, width / 4 * 3f)
//        if (textEnd > height && !textInAnimate) textAnimate(textHead, textHead - (textEnd - height))
//
//        canvas.drawRect(0f, 0f, width.toFloat(), center * 2, backgroundPaint)
//        linePaint.strokeWidth = radius / 2
//        canvas.drawLine(
//            if (isFirst) center else 0f,
//            center,
//            if (isEnd) center else width.toFloat(),
//            center,
//            linePaint
//        )
//        canvas.drawCircle(center, center, radius, circlePaint)
//    }
//
//    private fun drawText(x: Float, y: Float, canvas: Canvas, height: Float): Float {
//        var cy = y
//        for ((idx, c) in text.withIndex()) {
//            canvas.drawText(c.toString(), x, cy, textPaint)
//            if (idx != text.length - 1) cy += height * 1.1f
//        }
//        return cy
//    }
//
//    private fun statusAnimate() {
//        statusAnimation = ValueAnimator.ofFloat(0f, 1f)
//        val from = intArrayOf(Color.red(gray500), Color.green(gray500), Color.blue(gray500))
//        val to = intArrayOf(Color.red(red500), Color.green(red500), Color.blue(red500))
//        statusAnimation.let {
//            it.addUpdateListener { va ->
//                val percentage = va.animatedValue as Float
//                circlePaint.color = Color.rgb(
//                    (from[0] + (to[0] - from[0]) * percentage).toInt(),
//                    (from[1] + (to[1] - from[1]) * percentage).toInt(),
//                    (from[2] + (to[2] - from[2]) * percentage).toInt()
//                )
//                invalidate()
//            }
//            it.duration = 1500
//            it.repeatCount = -1
//            it.repeatMode = ValueAnimator.REVERSE
//            it.start()
//        }
//    }
//
//    private fun textAnimate(from: Float, to: Float) {
//        textInAnimate = true
//        textAnimation = ValueAnimator.ofFloat(from, to)
//        textAnimation.let {
//            it.addUpdateListener { va ->
//                textY = va.animatedValue as Float
//                invalidate()
//            }
//            it.addListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationRepeat(animation: Animator) {
//                    animation.pause()
//                    handler?.postDelayed({
//                        animation.resume()
//                    }, 2000)
//                }
//            })
//            it.duration = ((from - to) * 20).toLong()
//            it.repeatCount = -1
//            it.repeatMode = ValueAnimator.REVERSE
//            it.start()
//        }
//    }
//
//    override fun onVisibilityChanged(changedView: View, visibility: Int) {
//        super.onVisibilityChanged(changedView, visibility)
//        if (visibility == GONE) cancelAnimation()
//    }
//
//    override fun onDetachedFromWindow() {
//        super.onDetachedFromWindow()
//        cancelAnimation()
//        statusAnimation.cancel()
//    }
//
//    private fun cancelAnimation() {
//        handler.removeCallbacksAndMessages(null)
//        textAnimation.cancel()
//        textInAnimate = false
//    }
//
//    enum class Status {
//        NOT_ARRIVE, CURR, ARRIVED
//    }
//}