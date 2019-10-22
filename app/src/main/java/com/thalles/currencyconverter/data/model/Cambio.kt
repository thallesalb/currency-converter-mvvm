package com.thalles.currencyconverter.data.model

data class Cambio(
        val base: String,
        val date: String,
        val rates: HashMap<String, Double>
)