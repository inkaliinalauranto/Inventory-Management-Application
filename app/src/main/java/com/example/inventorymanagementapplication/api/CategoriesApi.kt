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
    /* API-method that fetches all category items from
    http://10.0.2.2:8000/api/v1/category. It returns a CategoriesRes
    data class object.
    */
    @GET("category/")
    suspend fun getCategories(): CategoriesRes

    /* API-method that fetches a category item according to the category id
    from http://10.0.2.2:8000/api/v1/category/{categoryId}. It returns
    a CategoryRes data class object.
    */
    @GET("category/{categoryId}")
    suspend fun getCategoryById(@Path("categoryId") categoryId: Int): CategoryRes

    /* API-method that modifies the name value of a category item according to
    the category id. The category item resource is applied from
    http://10.0.2.2:8000/api/v1/category/{categoryId}. It returns a
    CategoryRes data class object with the new value set by reqBody parameter.
    */
    @PUT("category/{categoryId}")
    suspend fun editCategory(
        @Path("categoryId") categoryId: Int,
        @Body reqBody: UpdateCategoryReq
    ): CategoryRes

    /* API-method that removes a category item according to the category id.
    The category item resource is retrieved from
    http://10.0.2.2:8000/api/v1/category/{categoryId}.
    */
    @DELETE("category/{categoryId}")
    suspend fun removeCategory(
        @Path("categoryId") categoryId: Int
    )
}