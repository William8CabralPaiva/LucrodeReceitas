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

    init {
        bindingLayout(attrs)
    }

    private fun bindingLayout(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArchRecipeSpinner)


            val text = typedArray.getText(R.styleable.ArchRecipeSpinner_arch_rs_hint_text) ?: ""
            binding.textView.hint = text
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

    private fun changeText() {
//        binding.spinner.addTextChangedListener {
//            isErrorEnabled = false
//            error = null
//        }
    }

    fun setError(errorText: String?) {
//        binding.biHint.run {
//            isErrorEnabled = true
//            error = errorText
//        }
    }


//    private fun hintTextColor() {
//        binding.spinner.text?.let { input ->
//            binding.spinner.setOnFocusChangeListener { _, hasFocus ->
//
//                val color = when {
//                    (input.isNotEmpty() && !hasFocus) -> {
//                        com.cabral.design.R.color.design_orange
//                    }
//
//                    !hasFocus -> {
//                        com.cabral.design.R.color.design_gray_dark
//                    }
//
//                    else -> {
//                        com.cabral.design.R.color.design_orange
//                    }
//                }
//
//                binding.biHint.defaultHintTextColor =
//                    ColorStateList.valueOf(getColor(color))
//
//            }
//
//        }
//    }


}