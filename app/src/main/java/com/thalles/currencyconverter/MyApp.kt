package com.thalles.currencyconverter

import android.app.Application
import com.thalles.currencyconverter.dependencyInjection.Modulos
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(mutableListOf(Modulos.conversor))
        }
    }
}