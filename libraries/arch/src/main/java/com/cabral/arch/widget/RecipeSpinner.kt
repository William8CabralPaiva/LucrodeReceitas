package com.cabral.arch.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import android.widget.Spinner
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import com.cabral.arch.R
import com.cabral.arch.databinding.ArchActionButtonBinding
import com.cabral.arch.databinding.ArchRecipeSpinnerBinding

class RecipeSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val binding = ArchRecipeSpinnerBinding
        .inflate(LayoutInflater.from(context), this, true)

    private var labelInput = ""

    init {
        bindingLayout(attrs)
    }

    private fun bindingLayout(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArchRecipeSpinner)


            val text = typedArray.getText(R.styleable.ArchRecipeSpinner_arch_rs_hint_text) ?: ""
            setLabelText(text.toString())

            changeText()

            typedArray.recycle()

        }
    }

    fun getSpinner(): AutoCompleteTextView {
        return binding.spinner
    }

    fun getText(): String? {
        return binding.spinner.text.toString()
    }

    fun clearInputText() {
        return binding.spinner.setText("")
    }

    fun setInputText(text: String) {
        return binding.spinner.setText(text)
    }

    private fun setLabelText(text: String) {
        binding.run {
            labelInput = text
            biHint.hint = labelInput
        }
    }

    private fun changeText() {
        binding.spinner.doOnTextChanged { _, _, _, _ ->
            binding.biHint.run {
                isErrorEnabled = false
                error = null
            }
        }
    }

    fun setError(errorText: String? = null) {
        binding.biHint.run {
            isErrorEnabled = true
            error = errorText ?: "Preencha o campo $labelInput corretamente"
        }
    }


}