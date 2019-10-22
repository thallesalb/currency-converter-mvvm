package com.thalles.currencyconverter.data.retrofit

import com.thalles.currencyconverter.data.model.Cambio
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ConversaoService {
    @GET("/latest")
    fun getCambio(@Query("base") base: String): Call<Cambio>
}