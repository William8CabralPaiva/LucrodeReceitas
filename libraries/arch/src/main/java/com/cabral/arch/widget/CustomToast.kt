package com.cabral.arch.widget

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.cabral.arch.databinding.ArchCustomToastBinding

class CustomToast(
    val context: Context,
    val message: String?,
    val icon: Int,
    val color: Int,
    val backgroundColor: Int,
    val alpha: Boolean = true
) {
    private val toast by lazy { Toast(context) }

    val inflater by lazy {
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
    val binding by lazy { ArchCustomToastBinding.inflate(inflater, null, false) }

    init {
        inflateToast()
    }

    private fun inflateToast() {
        binding.customToastTextTv.text = message

        if (color != -1) {
            binding.customToastIconIv.setColorFilter(
                ContextCompat.getColor(
                    context,
                    color
                )
            )
        }

        if (backgroundColor != -1) {
            binding.card.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    backgroundColor
                )
            )
        }

        if (icon != -1) {
            binding.customToastIconIv.setImageResource(icon)
        }

        binding.card.alpha = if (alpha) {
            0.8f
        } else {
            1f
        }
    }

    fun show() {
        toast.apply {
            setGravity(Gravity.FILL_HORIZONTAL, 0, 0)
            duration = Toast.LENGTH_LONG
            this.view = binding.root
            show()
        }
    }

    class Builder(private var context: Context) {
        private var message: String = ""
        private var icon: Int = -1
        private var color = -1
        private var backgroundColor = -1
        private var alpha = true

        fun message(message: String) = apply { this.message = message }
        fun icon(icon: Int) = apply { this.icon = icon }
        fun setIconColor(color: Int) = apply { this.color = color }
        fun setBackgroundColor(backgroundColor: Int) =
            apply { this.backgroundColor = backgroundColor }

        fun setAlpha(alpha: Boolean) = apply { this.alpha = alpha }
        fun build() = CustomToast(context, message, icon, color, backgroundColor, alpha)

    }

}
