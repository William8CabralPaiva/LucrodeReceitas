package com.cabral.arch.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.cabral.arch.R
import com.cabral.arch.databinding.ArchActionButtonBinding

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

    private val binding = ArchActionButtonBinding
        .inflate(LayoutInflater.from(context), this, true)

    init {
        bindingLayout(attrs)
    }

    private fun bindingLayout(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArchActionButton)

            val buttonType =
                typedArray.getEnum(R.styleable.ArchActionButton_arch_ab_type, ButtonType.SHAPE)

            val buttonText = typedArray.getText(R.styleable.ArchActionButton_arch_ab_text) ?: ""
            binding.textView.text = buttonText

            binding.container.background = getShape(buttonType)
            setTextColors(buttonType)

            typedArray.recycle()
        }
    }

    private fun getShape(buttonType: ButtonType): Drawable? {
        val drawable = when (buttonType) {
            ButtonType.SHAPE -> {
                R.drawable.arch_ripple_shape_orange_button
            }

            ButtonType.BORDER -> {
                R.drawable.arch_ripple_border_orange_button
            }
        }
        return AppCompatResources.getDrawable(context, drawable)
    }

    private fun setTextColors(buttonType: ButtonType) {
        val color = when (buttonType) {
            ButtonType.SHAPE -> {
                getColor(com.cabral.design.R.color.design_white)
            }

            ButtonType.BORDER -> {
                getColor(com.cabral.design.R.color.design_orange)
            }
        }
        //binding.progressCircularRing.setIconColor(color)
        binding.textView.setTextColor(color)
    }

    private fun getColor(@ColorRes color: Int): Int {
        return ContextCompat.getColor(context, color)
    }

    private inline fun <reified T : Enum<T>> TypedArray.getEnum(index: Int, default: T) =
        getInt(index, -1).let {
            if (it >= 0) enumValues<T>()[it] else default
        }

    fun startLoading() {
        binding.container.run {
            if (!isLoading && isEnabled) {
                isLoading = true
                isEnabled = false

                binding.textView.isVisible = false
                binding.progressCircular.isVisible = true

            }
        }
    }

    fun finishLoading(success: Boolean, hideIcon: Boolean = false) {
        binding.container.run {

            binding.iconResult.setImageDrawable(iconChecked(success))

            if (isLoading) {
                binding.progressCircular.animate().apply {
                    duration = 350
                }.withEndAction {
                    binding.progressCircular.isVisible = false
                    binding.iconResult.animate().apply {
                        duration = 350
                    }.withEndAction {
                        binding.iconResult.isVisible = true
                        binding.iconResult.animate().apply {
                            duration = 750
                        }.withEndAction {
                            hideIconShowText(hideIcon)
                        }.start()
                    }.start()
                }.start()
            }
        }
    }

    private fun hideIconShowText(hideIcon: Boolean) {
        if (hideIcon) {
            binding.iconResult.isVisible = false
            android.os.Handler(Looper.getMainLooper()).postDelayed({
                binding.textView.isVisible = true
                isLoading = false
                binding.container.isEnabled = true
            }, 250)
        }
    }

    private fun iconChecked(success: Boolean): Drawable? {
        return if (success) {
            AppCompatResources.getDrawable(context, R.drawable.arch_ic_checked)
        } else {
            AppCompatResources.getDrawable(context, R.drawable.arch_ic_unchecked)
        }
    }

    fun abSetOnClickListener(insideFunction: () -> Unit) {
        binding.container.setOnClickListener {
            insideFunction()
        }
    }

    enum class ButtonType(type: String) {
        SHAPE("shape"),
        BORDER("border")
    }

}