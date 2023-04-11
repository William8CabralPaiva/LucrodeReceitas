package com.cabral.lucrodereceitas.widget

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.IntegerRes
import com.cabral.lucrodereceitas.R

class ProgressRing(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var progress = 0f
    private var strokeWidth = resources.getDimension(R.dimen.default_stroke_width)
    private var backgroundStrokeWidth = 0f
    private var color = Color.WHITE
    private var backgroundColor = Color.WHITE
    private var tickColor = Color.WHITE

    private val startAngle = -90 //start angle
    private var rectF = RectF()
    private var mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mForegroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mContentPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mTickPath = Path()
    private var mCrossPath = Path()

    private var mProgressRunning = false
    private var mProgressStopped = false
    private var mFadingRunning = false
    private var mFadingStopped = false
    private var mIsDrawTick = false

    private var animatedValue = 0f
    private val animUpdateListener =  AnimatorUpdateListener { animation: ValueAnimator ->
        if (animation.isRunning) {
            animatedValue = animation.animatedValue.toString().toFloat()
            invalidate()
        }
    }

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val typedArray = context.theme
            .obtainStyledAttributes(attrs, R.styleable.ProgressRing, 0, 0)

        try {
            progress = typedArray.getFloat(R.styleable.ProgressRing_pr_progress, progress)
            color = typedArray.getColor(R.styleable.ProgressRing_pr_progressbar_color, color)
            backgroundColor = typedArray.getColor(
                R.styleable.ProgressRing_pr_background_progressbar_color,
                backgroundColor
            )
            tickColor = typedArray.getColor(R.styleable.ProgressRing_pr_tick_color, tickColor)
            strokeWidth =
                typedArray.getDimension(R.styleable.ProgressRing_pr_progressbar_width, strokeWidth)
            backgroundStrokeWidth = typedArray.getDimension(
                R.styleable.ProgressRing_pr_background_progressbar_width,
                backgroundStrokeWidth
            )
        } finally {
            typedArray.recycle()
        }

        mBackgroundPaint.setPoint(backgroundColor, backgroundStrokeWidth)
        mForegroundPaint.setPoint(backgroundColor, strokeWidth)
        mContentPaint.setPoint(backgroundColor, strokeWidth)
    }

    private fun Paint.setPoint(color: Int, widthStroke: Float) {
        this.color = color
        this.style = Paint.Style.STROKE
        this.strokeWidth = widthStroke
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mProgressRunning) {
            canvas.drawOval(rectF, mBackgroundPaint)
            canvas.drawArc(
                rectF,
                startAngle.toFloat(),
                360 * progress / 100,
                false,
                mForegroundPaint
            )
        } else if (mProgressStopped) {
            canvas.drawOval(rectF, mBackgroundPaint)
            if (mFadingRunning) {

                mContentPaint.alpha =
                    255 - (255 * animatedValue).toInt()
            } else if (mFadingStopped) {

                mContentPaint.alpha = 255
            }
            if (mIsDrawTick) {
                canvas.drawPath(mTickPath, mContentPaint)
            } else {
                canvas.drawPath(mCrossPath, mContentPaint)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val width = width
        val height = height
        mTickPath.reset()
        mTickPath.moveTo(
            (width * 0.35f).toInt().toFloat(),
            (height * 0.5f).toInt().toFloat()
        ) //starts the tick at (0.35,0.5)
        mTickPath.lineTo(
            (width * 0.45f).toInt().toFloat(),
            (height * 0.6f).toInt().toFloat()
        ) //draws a line to (0.45, 0.6) "\"
        mTickPath.lineTo(
            (width * 0.65f).toInt().toFloat(),
            (height * 0.4f).toInt().toFloat()
        ) //draws the other line to (0.65, 0.4) "/"
        mCrossPath.reset()
        mCrossPath.moveTo((width * 0.35f).toInt().toFloat(), (height * 0.35f).toInt().toFloat())
        mCrossPath.lineTo((width * 0.65f).toInt().toFloat(), (height * 0.65f).toInt().toFloat())
        mCrossPath.moveTo((width * 0.35f).toInt().toFloat(), (height * 0.65f).toInt().toFloat())
        mCrossPath.lineTo((width * 0.65f).toInt().toFloat(), (height * 0.35f).toInt().toFloat())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val min = Math.min(width, height)
        setMeasuredDimension(min, min)
        val highStroke =
            if (strokeWidth > backgroundStrokeWidth) strokeWidth else backgroundStrokeWidth
        rectF[0 + highStroke / 2, 0 + highStroke / 2, min - highStroke / 2] = min - highStroke / 2
    }

    fun setIconColor(color: Int){
        setColor(color)
        setBackgroundColor(color)
        setTickColor(color)
    }


    fun setProgress(progress: Float) {
        this.progress = if (progress <= 100) progress else 100F
        invalidate()
    }

    fun setColor(color: Int) {
        this.color = color
        mForegroundPaint.color = color
        invalidate()
        requestLayout()
    }

    fun getBackgroundColor(): Int {
        return backgroundColor
    }


    override fun setBackgroundColor(backgroundColor: Int) {
        this.backgroundColor = backgroundColor
        mBackgroundPaint.color = backgroundColor
        invalidate()
        requestLayout()
    }

    fun setTickColor(tickColor: Int) {
        this.tickColor = tickColor
        mContentPaint.color = tickColor
        invalidate()
        requestLayout()
    }

    private fun setProgressWithAnimation(progress: Float, duration: Int) {
        if (mProgressStopped || mFadingRunning || mFadingStopped) {
            return
        }
        val objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress)
        objectAnimator.duration = duration.toLong()
        objectAnimator.interpolator = DecelerateInterpolator()
        objectAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                mProgressRunning = true
                mProgressStopped = false
            }

            override fun onAnimationEnd(animator: Animator) {
                if (progress == 100f) {
                    mProgressRunning = false
                    mProgressStopped = true
                    fadingAnimation()
                }
            }

            override fun onAnimationCancel(animator: Animator) {
                mProgressRunning = false
                mProgressStopped = true
            }

            override fun onAnimationRepeat(animator: Animator) {}
        })
        objectAnimator.start()
    }

    private fun fadingAnimation() {
        if (mFadingRunning || mFadingStopped) {
            return
        }
        val valueAnimator = ValueAnimator.ofFloat(1f, 0f)
        valueAnimator.duration = DEFAULT_FADE_DURATION.toLong()
        valueAnimator.addUpdateListener(animUpdateListener)
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                animatedValue = 1f
                mFadingRunning = true
                mFadingStopped = false
            }

            override fun onAnimationEnd(animator: Animator) {
                mFadingRunning = false
                mFadingStopped = true
            }

            override fun onAnimationCancel(animator: Animator) {
                mFadingRunning = false
                mFadingStopped = true
            }

            override fun onAnimationRepeat(animator: Animator) {}
        })
        valueAnimator.start()
    }

    private fun setInfiniteProgressWithAnimation(progress: Float) {
        if (mProgressStopped || mFadingRunning || mFadingStopped) {
            return
        }
        val objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress)
        objectAnimator.duration = DEFAULT_INFINITE_SPIN_DURATION.toLong()
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.repeatMode = ValueAnimator.RESTART
        objectAnimator.interpolator = LinearInterpolator()
        objectAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                mProgressRunning = true
                mProgressStopped = false
            }

            override fun onAnimationEnd(animator: Animator) {
                mProgressRunning = false
                mProgressStopped = true
            }

            override fun onAnimationCancel(animator: Animator) {
                mProgressRunning = false
                mProgressStopped = true
            }

            override fun onAnimationRepeat(animator: Animator) {}
        })
        objectAnimator.start()
    }

    private fun showInfiniteLoading() {
        mProgressStopped = false
        progress = 0f
        mBackgroundPaint.alpha = 76
        setInfiniteProgressWithAnimation(100f)
    }

    fun showLoading(progress: Int = 75, duration: Int = DEFAULT_PROGRESS_DURATION) {
        reset()
        showInfiniteLoading()
    }

    fun showLoading(duration: Int, isSuccessful: Boolean) {
        mIsDrawTick = isSuccessful
        setProgressWithAnimation(100f, duration)
    }

    fun finishLoading(isSuccessful: Boolean) {
        mBackgroundPaint.alpha = 255
        mIsDrawTick = isSuccessful
        setProgressWithAnimation(100f, DEFAULT_FINISH_PROGRESS_DURATION)
    }

    fun finishInfiniteLoading(isSuccessful: Boolean) {
        mIsDrawTick = isSuccessful
        mProgressRunning = false
        mProgressStopped = true
    }

    private fun reset() {
        mProgressRunning = false
        mProgressStopped = false
        mFadingRunning = false
        mFadingStopped = false
        progress = 0f
        invalidate()
    }

    companion object {
        private const val DEFAULT_FINISH_PROGRESS_DURATION = 250
        private const val DEFAULT_FADE_DURATION = 250
        private const val DEFAULT_INFINITE_SPIN_DURATION = 8 * 1000
        private const val DEFAULT_PROGRESS_DURATION = 2000
    }
}