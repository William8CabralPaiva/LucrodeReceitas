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
    val backgroundColor: Int,
    val color: Int,
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


        if (icon != -1) {
            binding.customToastIconIv.setImageResource(icon)
        }

        if (backgroundColor != -1) {
            binding.card.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    backgroundColor
                )
            )
        }

    }

    fun show() {
        toast.apply {
            setGravity(Gravity.FILL_HORIZONTAL or Gravity.TOP, 0, 100)
            duration = Toast.LENGTH_SHORT
            this.view = binding.root
            show()
        }
    }

    class Builder(private var context: Context) {
        private var message: String = ""
        private var icon: Int = -1
        private var color = -1
        private var backgroundColor = -1

        fun message(message: String) = apply { this.message = message }
        fun icon(icon: Int) = apply { this.icon = icon }
        fun setIconColor(color: Int) = apply { this.color = color }

        fun setBackgroundColor(backgroundColor: Int) =
            apply { this.backgroundColor = backgroundColor }
        fun build() = CustomToast(context, message, icon,backgroundColor, color)

    }

}
