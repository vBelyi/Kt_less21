package com.example.kt_less21

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object ApiClient {
    private val httpClient = OkHttpClient.Builder().build()

    val client = Retrofit.Builder()
        .baseUrl("https://akabab.github.io")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()

    //Оновлена функція для асинхронних запитів з використанням корутин
    suspend inline fun <reified T> makeApiCall(crossinline call: () -> Response<T>): T {
        return withContext(Dispatchers.IO) {
            val response = call.invoke()
            if (response.isSuccessful) {
                response.body() ?: throw IOException("Response body is null")
            } else {
                throw IOException("Error: ${response.code()} ${response.message()}")
            }
        }
    }
}