package com.thalles.currencyconverter.data.retrofit

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> callback(preExecute: () -> Unit = {},
                 response: (response: Response<T>?) -> Unit = {},
                 failure: (throwable: Throwable?) -> Unit = {},
                 finished: () -> Unit = {}): Callback<T> {
    preExecute()
    return object : Callback<T> {
        override fun onResponse(call: Call<T>?, response: Response<T>?) {
            response(response)
            finished()
        }

        override fun onFailure(call: Call<T>?, t: Throwable?) {
            failure(t)
            finished()
        }
    }
}

fun <T> Response<T>?.defaultResponse(success: (t: T) -> Unit) {
    this?.body()?.let {
        success(it)
        return
    }
}

fun <T> Response<T>?.defaultFailure(failure: (t: JsonObject) -> Unit) {
    try {
        val json = this?.errorBody()?.string()
        val jsonObject = JsonParser().parse(json).asJsonObject
        jsonObject.addProperty("httpCode", this?.code())
        failure(jsonObject)
    } catch (e: Exception) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("message", "Desculpe =( \n aconteceu um erro inesperado\"}")
        failure(jsonObject)
    }
}

fun Throwable?.defaultError(failure: (throwable: Throwable) -> Unit) {
    this?.let {
        failure(it)
    }
}
