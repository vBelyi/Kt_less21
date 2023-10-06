package com.example.kt_less21

import retrofit2.http.GET

interface ApiInterface {

    @GET("/superhero-api/api/all.json")
    suspend fun getRequest(): List<GetResponse>
}