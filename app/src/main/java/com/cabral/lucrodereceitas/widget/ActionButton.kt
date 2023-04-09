package com.cabral.lucrodereceitas.widget

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.cabral.lucrodereceitas.R
import com.cabral.lucrodereceitas.databinding.ActionButtonBinding

class ActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

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
                getColor(R.color.white)
            }
            ButtonType.BORDER -> {
                getColor(R.color.orange)
            }
        }
        val colorState = ColorStateList.valueOf(color)
        binding.progressBar.indeterminateTintList = colorState
        binding.textView.setTextColor(color)
    }

    private fun getColor(@ColorRes color: Int): Int {
        return ContextCompat.getColor(context, color);
    }

    private inline fun <reified T : Enum<T>> TypedArray.getEnum(index: Int, default: T) =
        getInt(index, -1).let {
            if (it >= 0) enumValues<T>()[it] else default
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