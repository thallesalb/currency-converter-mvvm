package com.thalles.currencyconverter.dependencyInjection

import com.thalles.currencyconverter.data.repository.ConversorRepository
import com.thalles.currencyconverter.data.retrofit.RetrofitBuilder
import com.thalles.currencyconverter.viewmodel.ConversorViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Modulos {
    val conversor = module {
        single { RetrofitBuilder }
        factory { ConversorRepository(get()) }
        viewModel { ConversorViewModel(get()) }
    }
}