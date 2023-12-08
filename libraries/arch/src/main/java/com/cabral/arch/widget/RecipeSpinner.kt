package com.cabral.arch.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import android.widget.Spinner
import com.cabral.arch.R
import com.cabral.arch.databinding.ArchActionButtonBinding
import com.cabral.arch.databinding.ArchRecipeSpinnerBinding

class RecipeSpinner@JvmOverloads constructor(
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

    fun getSpinner():AutoCompleteTextView{
        return binding.spinner
    }

}