package com.cabral.arch.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.cabral.arch.R
import com.cabral.arch.databinding.ArchBorderInputViewBinding
import com.cabral.arch.databinding.ArchTitleViewBinding

class TitleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val binding = ArchTitleViewBinding
        .inflate(LayoutInflater.from(context), this, true)

    init {
        bindingLayout(attrs)
    }

    private fun bindingLayout(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArchTitleView)

            val text = typedArray.getText(R.styleable.ArchTitleView_arch_title) ?: ""
            setTitle(text.toString())

            val icon = typedArray.getResourceId(R.styleable.ArchTitleView_arch_icon, 0)
            setIcon(icon)

            typedArray.recycle()

        }
    }

    fun setTitle(text: String) {
        if (text.isNotEmpty()) {
            val textTitle = "$text "
            binding.title.text = textTitle
        }
    }

    private fun setIcon(iconId: Int) {
        if (iconId != 0) {
            binding.icon.run {
                isVisible = true
                setImageResource(iconId)
            }
        }
    }
}