package com.example.inventorymanagementapplication.api

import com.example.inventorymanagementapplication.model.CategoriesRes
import com.example.inventorymanagementapplication.model.CategoryRes
import com.example.inventorymanagementapplication.model.UpdateCategoryReq
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

// A retrofit instance is created to handle API querys and responses:
private val retrofit = createClient()

/* A retrofit service interface implementation is created from CategoriesApi
interface. The service implements the methods of the interface:
*/
val categoriesService: CategoriesApi = retrofit.create(CategoriesApi::class.java)


interface CategoriesApi {
    /* Comments... */
    @GET("category/")
    suspend fun getCategories() : CategoriesRes

    /* Comments... */
    @GET("category/{categoryId}")
    suspend fun getCategoryById(@Path("categoryId") categoryId: Int): CategoryRes

    /* Comments... */
    @PUT("category/{categoryId}")
    suspend fun editCategory(
        @Path("categoryId") categoryId: Int,
        @Body reqBody: UpdateCategoryReq
    ): CategoryRes

    /* Comments... */
    @DELETE("category/{categoryId}")
    suspend fun removeCategory(
        @Path("categoryId") categoryId: Int
    )
}