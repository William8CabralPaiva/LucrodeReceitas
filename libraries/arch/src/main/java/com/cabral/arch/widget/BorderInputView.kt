package com.cabral.arch.widget

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.cabral.arch.R
import com.cabral.arch.databinding.BorderInputViewBinding
import com.google.android.material.textfield.TextInputLayout

class BorderInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val binding = BorderInputViewBinding
        .inflate(LayoutInflater.from(context), this, true)

    init {
        bindingLayout(attrs)
    }

    private fun bindingLayout(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BorderInputView)

            val hint = typedArray.getText(R.styleable.BorderInputView_ei_label) ?: ""
            setLabelText(hint.toString())

            val color =
                typedArray.getEnum(R.styleable.BorderInputView_ei_color, ColorType.ORANGE)
            setDefaultColor(color)

            val BIInputType =
                typedArray.getEnum(R.styleable.BorderInputView_ei_type, BIInputType.TEXT);
            setInputType(BIInputType)

            typedArray.recycle()
        }

    }

    private fun setInputType(BIInputType: BIInputType) {
        binding.run {
            when (BIInputType) {
                BorderInputView.BIInputType.TEXT -> {
                    biTextInput.inputType = android.text.InputType.TYPE_CLASS_TEXT
                    biHint.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                }

                BorderInputView.BIInputType.PASSWORD -> {
                    biHint.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                    biTextInput.inputType = android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
            }
        }
    }

    private inline fun <reified T : Enum<T>> TypedArray.getEnum(index: Int, default: T) =
        getInt(index, -1).let {
            if (it >= 0) enumValues<T>()[it] else default
        }

    enum class ColorType(val colorType: String) {
        ORANGE("orange")
    }

    enum class BIInputType(val inputType: String) {
        TEXT("text"),
        PASSWORD("password")
    }

    fun setLabelText(text: String) {
        binding.run {
            biHint.hint = text
            biTextInput.hint = text
        }
    }

    fun setInputText(text: String) {
        binding.biTextInput.setText(text)
    }

    private fun controlColors(colorType: ColorType): ColorStateList {
        val states = arrayOf(
            intArrayOf(android.R.attr.state_enabled), // enabled
            intArrayOf(-android.R.attr.state_enabled), // disabled
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_pressed)  // pressed
        )

        val colors = when (colorType) {
            ColorType.ORANGE -> {
                intArrayOf(
                    getColor(com.cabral.design.R.color.orange),
                    getColor(com.cabral.design.R.color.orange_light),
                    getColor(com.cabral.design.R.color.orange),
                    getColor(com.cabral.design.R.color.orange_light)
                )
            }
        }

        return ColorStateList(states, colors)
    }

    fun setDefaultColor(colorType: ColorType) {
        try {
            val colorState = controlColors(colorType)
            binding.biHint.apply {
                hintTextColor = colorState
                defaultHintTextColor = colorState
                setBoxStrokeColorStateList(colorState)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun getColor(@ColorRes color: Int): Int {
        return ContextCompat.getColor(context, color);
    }
}