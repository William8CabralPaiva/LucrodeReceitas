package com.cabral.arch.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
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

    private var drawable: Drawable? = null

    var labelInput = ""

    init {
        bindingLayout(attrs)
    }

    private fun bindingLayout(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArchBorderInputView)
            setupIds()

            val hint = typedArray.getText(R.styleable.ArchBorderInputView_arch_ei_label) ?: ""
            setLabelText(hint.toString())

            val BIInputType =
                typedArray.getEnum(R.styleable.ArchBorderInputView_arch_ei_type, BIInputType.TEXT)
            setInputType(BIInputType)

            val drawableTop =
                typedArray.getDrawable(R.styleable.ArchBorderInputView_arch_drawable_top)

            changeText()

            setDrawableTop(drawableTop)

            typedArray.recycle()
        }

    }

    private fun setupIds() {
        with(binding) {
            resources.getResourceName(id).run {
                substring(lastIndexOf("/") + 1).hashCode()
            }.let { id ->
                biTextInput.id = id
            }.also {
                biTextInput.id = id
            }
        }
    }

    private fun setInputType(BIInputType: BIInputType) {
        binding.run {
            when (BIInputType) {
                BorderInputView.BIInputType.TEXT -> {
                    biTextInput.inputType = InputType.TYPE_CLASS_TEXT
                    biHint.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                }

                BorderInputView.BIInputType.PASSWORD -> {
                    biHint.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                    biTextInput.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }

                BorderInputView.BIInputType.NUMBER -> {
                    biTextInput.inputType = InputType.TYPE_CLASS_NUMBER or
                            InputType.TYPE_NUMBER_FLAG_DECIMAL or
                            InputType.TYPE_NUMBER_FLAG_SIGNED

                    biHint.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                }
            }
        }
    }

    private inline fun <reified T : Enum<T>> TypedArray.getEnum(index: Int, default: T) =
        getInt(index, -1).let {
            if (it >= 0) enumValues<T>()[it] else default
        }

    enum class BIInputType(val inputType: String) {
        TEXT("text"),
        PASSWORD("password"),
        NUMBER("number")
    }

    private fun setDrawableTop(drawableTop: Drawable?) {
        drawableTop?.let {
            drawable = drawableTop
            binding.biTextInput.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                drawableTop,
                null
            )
        }
    }

    private fun setLabelText(text: String) {
        binding.run {
            labelInput = text
            biHint.hint = labelInput
        }
    }

    fun setInputText(text: String) {
        binding.biTextInput.setText(text)
    }

    fun clearInputText() {
        binding.biTextInput.setText("")
    }

    fun setFocus() {
        binding.biTextInput.requestFocus()
    }


    fun getText(): String {
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

    fun setTextChangedListener(insideFunction: () -> Unit) {
        binding.biTextInput.addTextChangedListener {
            insideFunction()
        }
    }

    fun setError(errorText: String? = null) {
        binding.biHint.run {
            isErrorEnabled = true
            error = errorText ?: "Preencha o campo $labelInput corretamente".also {
                binding.biTextInput.isCursorVisible=true
            }
        }
    }


    private fun getColor(@ColorRes color: Int): Int {
        return ContextCompat.getColor(context, color)
    }
}