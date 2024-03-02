package com.example.inventorymanagementapplication.api

import com.example.inventorymanagementapplication.model.CategoriesRes
import retrofit2.http.GET

// A retrofit instance is created to handle API querys and responses:
private val retrofit = createClient()

/* A retrofit service interface implementation is created from CategoriesApi
interface. The service implements the method of the interface:
*/
val categoriesService = retrofit.create(CategoriesApi::class.java)


interface CategoriesApi {
    @GET("category/")
    suspend fun getCategories() : CategoriesRes
}