package com.thalles.currencyconverter.resources

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import java.text.NumberFormat
import java.util.*

class Utils {
    companion object {
        fun Spinner.add(
                moedas: MutableList<String>,
                context: Context,
                listener: AdapterView.OnItemSelectedListener
        ) = this.apply {
            adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, moedas)
            setSelection(0, true)
            onItemSelectedListener = listener
        }

        fun EditText.moneyMask(doOnTextChange: (String, TextWatcher) -> Unit) =
                this.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        p0?.let { doOnTextChange(it.toString(), this) }
                    }
                })

        fun Double.formatCurrency(currency: String): String {
            val format = NumberFormat.getCurrencyInstance()
            format.currency = Currency.getInstance(currency)
            return format.format(this)
        }

        fun String.clearNoNumbers() = this.replace("[^0-9]".toRegex(), "")
    }
}