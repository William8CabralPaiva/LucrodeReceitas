package com.cabral.arch.widget

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.cabral.arch.R
import com.cabral.arch.databinding.ArchBorderInputViewBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class BorderInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val binding = ArchBorderInputViewBinding
        .inflate(LayoutInflater.from(context), this, true)

    init {
        bindingLayout(attrs)
    }

    private fun bindingLayout(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArchBorderInputView)

            val hint = typedArray.getText(R.styleable.ArchBorderInputView_arch_ei_label) ?: ""
            setLabelText(hint.toString())

            val color =
                typedArray.getEnum(R.styleable.ArchBorderInputView_arch_ei_color, ColorType.ORANGE)
            //     setDefaultColor(color)

            val BIInputType =
                typedArray.getEnum(R.styleable.ArchBorderInputView_arch_ei_type, BIInputType.TEXT);
            setInputType(BIInputType)
            hintTextColor()
            changeText()
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
                    biTextInput.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }

                BorderInputView.BIInputType.NUMBER -> {
                    biTextInput.inputType = android.text.InputType.TYPE_CLASS_NUMBER or
                            android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL or
                            android.text.InputType.TYPE_NUMBER_FLAG_SIGNED

                    biHint.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
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
        PASSWORD("password"),
        NUMBER("number")
    }

    private fun setLabelText(text: String) {
        binding.run {
            biHint.hint = text
            //biTextInput.hint = text
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
                    getColor(com.cabral.design.R.color.design_orange),
                    getColor(com.cabral.design.R.color.design_orange_light),
                    getColor(com.cabral.design.R.color.design_orange),
                    getColor(com.cabral.design.R.color.design_orange_light)
                )
            }
        }

        return ColorStateList(states, colors)
    }

    private fun controlColors2(colorType: ColorType): ColorStateList {
        val states = arrayOf(
            intArrayOf(android.R.attr.state_enabled), // enabled
            intArrayOf(-android.R.attr.state_enabled), // enabled
        )

        val colors = when (colorType) {
            ColorType.ORANGE -> {
                intArrayOf(
                    getColor(com.cabral.design.R.color.design_orange),
                )
            }
        }

        return ColorStateList(states, colors)
    }

    private fun hintTextColor() {
        binding.biTextInput.text?.let { input ->
            binding.biTextInput.setOnFocusChangeListener { _, hasFocus ->

                val color = when {
                    (input.isNotEmpty() && !hasFocus) -> {
                        com.cabral.design.R.color.design_orange
                    }

                    !hasFocus -> {
                        com.cabral.design.R.color.design_gray_dark
                    }

                    else -> {
                        com.cabral.design.R.color.design_orange
                    }
                }

                binding.biHint.defaultHintTextColor =
                    ColorStateList.valueOf(getColor(color))

            }

        }
    }

    fun setDefaultColor(colorType: ColorType) {
        try {
            val colorState = controlColors(colorType)
            val colorState2 = controlColors2(colorType)
            binding.biHint.apply {
                hintTextColor = colorState2

                defaultHintTextColor = colorState2
                setBoxStrokeColorStateList(colorState)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun getText(): String? {
        return binding.biTextInput.text.toString()
    }

    fun getHint(): TextInputLayout {
        return binding.biHint
    }

    fun getTextInput(): TextInputEditText {
        return binding.biTextInput
    }

    private fun changeText() {
        binding.biTextInput.doOnTextChanged { _, _, _, _ ->
            binding.biHint.run {
                isErrorEnabled = false
                error = null
            }
        }
    }

    fun setError(errorText: String?) {
        binding.biHint.run {
            isErrorEnabled = true
            error = errorText
        }
    }


    private fun getColor(@ColorRes color: Int): Int {
        return ContextCompat.getColor(context, color);
    }
}