package com.cabral.arch.widget

import android.app.AlertDialog
import android.content.Context
import com.cabral.arch.R

class CustomAlertDialog private constructor(
    val context: Context,
    var title: String,
    var message: String,
    var negativeButton: String?,
    var positiveButton: String?,
    var negativeFunction: () -> Unit,
    var positiveFunction: () -> Unit,
) {

    private val alertBuilder by lazy {
        AlertDialog.Builder(
            context,
            com.cabral.design.R.style.DesignAlertDialogTheme
        )
    }

    init {
        alertBuilder.run {
            setTitle(title)
            setMessage(message)
            positiveButton?.let {
                setPositiveButton(positiveButton) { _, _ ->
                    positiveFunction()
                }
            }
            negativeButton?.let {
                setNegativeButton(negativeButton) { _, _ ->
                    negativeFunction()
                }
            }

        }

    }

    fun show() {
        alertBuilder.show()
    }

    class Builder(private var context: Context) {

        private var title: String = ""
        private var message: String = ""
        private var positiveButton: String = ""
        private var negativeButton: String = ""
        private var positiveFunction: () -> Unit = {}
        private var negativeFunction: () -> Unit = {}

        fun title(title: String) = apply { this.title = title }
        fun message(message: String) = apply { this.message = message }
        fun positiveButton(text: String) = apply { this.positiveButton = text }
        fun negativeButton(text: String) = apply { this.negativeButton = text }
        fun positiveFunction(positiveFunction: () -> Unit) =
            apply { this.positiveFunction = positiveFunction }

        fun negativeFunction(negativeFunction: () -> Unit) =
            apply {
                this.negativeFunction = negativeFunction
            }

        fun build() = CustomAlertDialog(
            context,
            title,
            message,
            negativeButton,
            positiveButton,
            negativeFunction,
            positiveFunction,
        )


    }
}