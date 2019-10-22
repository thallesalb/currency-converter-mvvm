package com.thalles.currencyconverter.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.thalles.currencyconverter.R
import com.thalles.currencyconverter.resources.Utils.Companion.add
import com.thalles.currencyconverter.resources.Utils.Companion.clearNoNumbers
import com.thalles.currencyconverter.resources.Utils.Companion.formatCurrency
import com.thalles.currencyconverter.resources.Utils.Companion.moneyMask
import com.thalles.currencyconverter.viewmodel.ConversorViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class ConversorActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val conversorViewModel: ConversorViewModel by viewModel()
    private var current = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initObservers()

        editText.moneyMask { str, watcher ->
            if (TextUtils.isEmpty(str.clearNoNumbers())) {
                editText2.text?.clear()
                return@moneyMask
            }

            if (str != current) {
                editText.removeTextChangedListener(watcher)
                val cleanString = str.clearNoNumbers()
                val parsed = cleanString.toDouble() / 100
                current = parsed.formatCurrency(spinner.selectedItem.toString())
                editText.setText(current)
                editText.setSelection(current.length)
                editText.addTextChangedListener(watcher)
            }

            val valor = str.clearNoNumbers().toDouble() / 100
            conversorViewModel.converter(valor, spinner2.selectedItem.toString())
        }

        conversorViewModel.moedaBase =
                if (conversorViewModel.moedaBase.isNotEmpty()) conversorViewModel.moedaBase
                else Currency.getInstance(Locale.getDefault()).currencyCode
    }

    /***** callbacks spinners *****/
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        p0?.let { adapterView ->
            when (adapterView) {
                spinner -> conversorViewModel.moedaBase = spinner.selectedItem.toString()
                spinner2 -> {
                    input2.hint = spinner2.selectedItem.toString()
                    val textValor = editText.text.toString()
                    if (!TextUtils.isEmpty(textValor)) {
                        val valor = textValor.clearNoNumbers().toDouble() / 100
                        conversorViewModel.converter(valor, spinner2.selectedItem.toString())
                    }
                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    /***** fim callbacks spinners *****/

    private fun initObservers() {
        conversorViewModel.isLoading.observe(this, Observer {
            it?.let { loading -> progressBar.visibility = if (loading) View.VISIBLE else View.GONE }
        })

        conversorViewModel.snackbar.observe(this, Observer {
            it?.let { erro ->
                habilitarUI(TextUtils.isEmpty(erro))
                if (!TextUtils.isEmpty(erro)) {
                    Snackbar.make(spinner, erro, Snackbar.LENGTH_INDEFINITE)
                            .setAction("Tentar Novamente") { conversorViewModel.getMoedas() }
                            .show()
                    return@let
                }
            }
        })

        conversorViewModel.moedas.observe(this, Observer { it?.let { moedas -> updateUI(moedas) } })
        conversorViewModel.valorFormatado.observe(this, Observer { it?.let { valor -> editText2.setText(valor) } })
    }

    private fun updateUI(moedas: MutableList<String>) {
        editText.text?.clear()
        editText2.text?.clear()

        spinner.add(moedas, this, this)
        spinner2.add(moedas, this, this)

        input.hint = conversorViewModel.moedaBase
        input2.hint = spinner2.selectedItem.toString()
    }

    private fun habilitarUI(enable: Boolean) {
        editText.isEnabled = enable
        editText2.isEnabled = enable
        spinner.isEnabled = enable
        spinner2.isEnabled = enable
        input.isEnabled = enable
        input2.isEnabled = enable
    }
}