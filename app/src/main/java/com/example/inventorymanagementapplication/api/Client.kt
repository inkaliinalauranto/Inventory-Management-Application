package com.example.inventorymanagementapplication.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/* A retrofit instance is created to handle API querys and responses.
A separate retrofit instance is created for each file of API package,
through which the API query can be executed. Retrofit is a library for
HTTP queries and for handling responses in Android applications.
*/
val retrofit = createClient()

fun createClient(): Retrofit {
    return Retrofit.Builder().baseUrl("http://10.0.2.2:8000/api/v1/")
        .addConverterFactory(GsonConverterFactory.create()).build()
}