package com.cabral.arch

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale


class MoneyTextMask : TextWatcher {
    private val editTextWeakReference: WeakReference<EditText>
    private val locale = Locale("pt", "BR")

    constructor(editText: EditText, locale: Locale?) {
        this.editTextWeakReference = WeakReference(editText)
    }

    constructor(editText: EditText) {
        this.editTextWeakReference = WeakReference(editText)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(editable: Editable) {
        val editText = editTextWeakReference.get() ?: return
        editText.removeTextChangedListener(this)
        val parsed = parseToBigDecimal(editable.toString(), locale)
        parsed?.let {
            val formatted = NumberFormat.getCurrencyInstance(locale).format(it)

            // NumberFormat.getNumberInstance(locale).format(parsed); // sem o simbolo de moeda
            editText.setText(formatted)
            editText.setSelection(formatted.length)
        } ?: run {
            val text = "0.00"
            editText.setText(text)
            editText.setSelection(text.length)
        }
        editText.addTextChangedListener(this)
    }

    private fun parseToBigDecimal(value: String, locale: Locale): BigDecimal? {
        try {
            val replaceable = java.lang.String.format(
                "[%s,.\\s]",
                NumberFormat.getCurrencyInstance(locale).currency.symbol
            )

            val cleanString = value.replace(replaceable.toRegex(), "")

            return BigDecimal(cleanString).setScale(
                2, BigDecimal.ROUND_FLOOR
            ).divide(
                BigDecimal(100), BigDecimal.ROUND_FLOOR
            )
        } catch (_: Exception) {
            return null
        }
    }
}