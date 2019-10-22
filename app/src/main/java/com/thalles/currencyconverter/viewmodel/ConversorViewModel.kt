package com.thalles.currencyconverter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thalles.currencyconverter.data.model.Cambio
import com.thalles.currencyconverter.data.repository.ConversorRepository
import com.thalles.currencyconverter.resources.Utils.Companion.formatCurrency
import java.util.*

class ConversorViewModel(private val conversorRepository: ConversorRepository) : ViewModel() {
    private var rates: HashMap<String, Double>? = null
    private val _moedas = MutableLiveData<MutableList<String>>()
    private val _valorFormatado = MutableLiveData<String>()
    private val _buscandoDados = MutableLiveData<Boolean>()
    private val _snackbar = MutableLiveData<String>()

    val moedas: LiveData<MutableList<String>>
        get() = _moedas

    val valorFormatado: LiveData<String>
        get() = _valorFormatado

    val isLoading: LiveData<Boolean>
        get() = _buscandoDados

    val snackbar: LiveData<String>
        get() = _snackbar

    var moedaBase = ""
        set(value) {
            if (value != field) {
                field = value
                getMoedas()
            }
        }

    fun getMoedas() {
        conversorRepository.getMoedas(
                preExecute = { _buscandoDados.value = true },
                base = moedaBase,
                success = { onGetMoedasRequestSuccess(it) },
                failure = { _snackbar.value = "Erro ao buscar dados" },
                error = { _snackbar.value = "Erro ao buscar dados" },
                finished = { _buscandoDados.value = false }
        )
    }

    fun converter(valor: Double, moeda: String) {
        try {
            val rate = rates?.get(moeda) ?: 0.0
            val valorConvertido = valor * rate
            _valorFormatado.value = valorConvertido.formatCurrency(moeda)
        } catch (ex: NumberFormatException) {
            _valorFormatado.value = null
        }
    }

    private fun onGetMoedasRequestSuccess(cambio: Cambio) {
        _snackbar.value = ""

        if (moedaBase == "EUR")
            cambio.rates["EUR"] = 1.0

        rates = cambio.rates
        _moedas.value = rates?.keys?.sortedBy { it == moedaBase }?.asReversed()?.toMutableList()
                ?: mutableListOf()
    }
}