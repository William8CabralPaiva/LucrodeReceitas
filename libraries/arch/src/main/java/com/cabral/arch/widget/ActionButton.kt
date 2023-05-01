package com.cabral.arch.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.cabral.arch.R
import com.cabral.arch.databinding.ActionButtonBinding

class ActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    val DURATION: Long = 750
    var isLoading = false
    var defaultColor: Int = 0

    private val buttonShape by lazy { GradientDrawable() }

    @ColorInt
    private var buttonColor: Int = -1

    private val binding = ActionButtonBinding
        .inflate(LayoutInflater.from(context), this, true)

    init {
        bindingLayout(attrs)
    }

    private fun bindingLayout(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ActionButton)

            val buttonType = typedArray.getEnum(R.styleable.ActionButton_ab_type, ButtonType.SHAPE)

            val buttonText = typedArray.getText(R.styleable.ActionButton_ab_text) ?: ""
            binding.textView.text = buttonText

            binding.root.background = getShape(buttonType)
            setTextColors(buttonType)

            typedArray.recycle()
        }
    }

    private fun getShape(buttonType: ButtonType): Drawable? {
        val drawable = when (buttonType) {
            ButtonType.SHAPE -> {
                R.drawable.ripple_shape_orange_button
            }

            ButtonType.BORDER -> {
                R.drawable.ripple_border_orange_button
            }
        }
        return AppCompatResources.getDrawable(context, drawable)
    }

    private fun setTextColors(buttonType: ButtonType) {
        val color = when (buttonType) {
            ButtonType.SHAPE -> {
                getColor(com.cabral.design.R.color.white)
            }

            ButtonType.BORDER -> {
                getColor(com.cabral.design.R.color.orange)
            }
        }
        //binding.progressRing.setIconColor(color)
        binding.textView.setTextColor(color)
    }

    private fun getColor(@ColorRes color: Int): Int {
        return ContextCompat.getColor(context, color);
    }

    private inline fun <reified T : Enum<T>> TypedArray.getEnum(index: Int, default: T) =
        getInt(index, -1).let {
            if (it >= 0) enumValues<T>()[it] else default
        }

    fun startLoading() {
        binding.root.run {
            if (!isLoading && isEnabled) {
                isLoading = true
                isEnabled = false

                val disappear = disappearText()
                binding.textView.startAnimation(disappear)

            }
        }
    }

    fun finishLoading(success: Boolean, backText: Boolean = false) {
        binding.root.run {

            if (isLoading) {
                isLoading = false
                isEnabled = true

                if (!backText) {
                    //binding.progressRing.finishLoading(success)
                } else {

                    val appear = appearProgress(success)
                    //binding.progressRing.startAnimation(appear)
                }
            }
        }
    }

    private fun appearProgress(success: Boolean): AlphaAnimation {
        val appear = AlphaAnimation(1f, 0f)
        appear.duration = 1350

        appear.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                //binding.progressRing.finishLoading(success)
            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.textView.isVisible = true
                //binding.progressRing.isVisible = false
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
        return appear
    }


    private fun disappearText(): AlphaAnimation {
        val disappear = AlphaAnimation(1f, 0f)
        disappear.duration = DURATION

        disappear.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.textView.isVisible = false
                //binding.progressRing.showLoading()
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
        return disappear
    }

    fun abSetOnClickListener(insideFunction: () -> Unit) {
        binding.root.setOnClickListener {
            insideFunction()
        }
    }

    enum class ButtonType(type: String) {
        SHAPE("shape"),
        BORDER("border")
    }

}