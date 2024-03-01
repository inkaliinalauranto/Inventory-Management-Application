package com.example.inventorymanagementapplication.api

import com.example.inventorymanagementapplication.model.CategoriesRes
import retrofit2.http.GET

// A retrofit instance is created to handle API querys and responses:
private val _retrofit = createClient()

/* A retrofit service interface implementation is created from CategoriesApi
interface:
*/
val categoriesService = _retrofit.create(CategoriesApi::class.java)


interface CategoriesApi {
    @GET("category/")
    suspend fun getCategories() : CategoriesRes
}