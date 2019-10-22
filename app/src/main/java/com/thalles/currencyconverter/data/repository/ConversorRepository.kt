package com.thalles.currencyconverter.data.repository

import com.google.gson.JsonObject
import com.thalles.currencyconverter.data.model.Cambio
import com.thalles.currencyconverter.data.retrofit.*

class ConversorRepository(private val retrofit: RetrofitBuilder) {
    fun getMoedas(
            base: String,
            preExecute: () -> Unit = {},
            success: (cambio: Cambio) -> Unit = {},
            failure: (json: JsonObject) -> Unit = {},
            error: (throwable: Throwable) -> Unit = {},
            finished: () -> Unit = {}
    ) {
        val call = retrofit
                .conversaoService()
                .getCambio(base)

        call.enqueue(
                callback(
                        preExecute = preExecute,
                        finished = finished,
                        response = {
                            if (it!!.isSuccessful) {
                                it.defaultResponse(success)
                            } else it.defaultFailure(failure)
                        },
                        failure = { it.defaultError(error) }
                )
        )
    }
}