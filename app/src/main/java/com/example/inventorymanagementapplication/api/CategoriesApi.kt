package com.example.inventorymanagementapplication.api

import com.example.inventorymanagementapplication.model.AccountRes
import com.example.inventorymanagementapplication.model.AddCategoryReq
import com.example.inventorymanagementapplication.model.AddRentalItemReq
import com.example.inventorymanagementapplication.model.AuthReq
import com.example.inventorymanagementapplication.model.AuthRes
import com.example.inventorymanagementapplication.model.CategoriesRes
import com.example.inventorymanagementapplication.model.CategoryRes
import com.example.inventorymanagementapplication.model.RentalItemRes
import com.example.inventorymanagementapplication.model.RentalItemRes2
import com.example.inventorymanagementapplication.model.RentalItemsRes
import com.example.inventorymanagementapplication.model.UpdateCategoryReq
import com.example.inventorymanagementapplication.model.UpdateRentalItemReq
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// A retrofit instance is created to handle API querys and responses:
private val retrofit = createClient()

/* A retrofit service interface implementation is created from CategoriesApi
interface. The service implements the methods of the interface:
*/
val categoriesService: CategoriesApi = retrofit.create(CategoriesApi::class.java)


interface CategoriesApi {
    @POST("auth/register")
    suspend fun register(@Body req: AuthReq)

    @GET("auth/account")
    suspend fun getAccount(@Header("Authorization") bearerToken: String): AccountRes

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") bearerToken: String)

    @POST("auth/login")
    suspend fun login(@Body req: AuthReq): AuthRes

    /* An API-method that fetches all category items from
    http://10.0.2.2:8000/api/v1/category. It returns a CategoriesRes
    data class object.
    */
    @GET("category/")
    suspend fun getCategories(): CategoriesRes

    /* An API-method that fetches a category item according to the category id
    from http://10.0.2.2:8000/api/v1/category/{categoryId}. It returns
    a CategoryRes data class object.
    */
    @GET("category/{categoryId}")
    suspend fun getCategoryById(@Path("categoryId") categoryId: Int): CategoryRes

    /* An API-method that creates a new category item. The category item is
    added to http://10.0.2.2:8000/api/v1/category/. It returns a CategoryRes
    data class object with the created value.
    */
    @POST("category/")
    suspend fun addCategory(
        @Body reqBody: AddCategoryReq
    ): CategoryRes

    @POST("category/{categoryId}/items/")
    suspend fun addRentalItem(
        @Path("categoryId") categoryId: Int,
        @Body reqBody: AddRentalItemReq
    ): RentalItemRes2

    /* An API-method that modifies the name value of a category item according
    to the category id. The category item resource is applied from
    http://10.0.2.2:8000/api/v1/category/{categoryId}. It returns a
    CategoryRes data class object with the new value set by reqBody parameter.
    */
    @PUT("category/{categoryId}/")
    suspend fun editCategory(
        @Path("categoryId") categoryId: Int,
        @Body reqBody: UpdateCategoryReq
    ): CategoryRes

    /* An API-method that removes a category item according to the category id.
    The category item resource is retrieved from
    http://10.0.2.2:8000/api/v1/category/{categoryId}.
    */
    @DELETE("category/{categoryId}/")
    suspend fun removeCategory(
        @Path("categoryId") categoryId: Int
    )

    /* An API-method that fetches a rental item according to the category id
    from http://10.0.2.2:8000/api/v1/category/{categoryId}/items. It returns
    a RentalItemsRes data class object.
    */
    @GET("category/{categoryId}/items/")
    suspend fun getRentalItemsByCategoryId(@Path("categoryId") categoryId: Int): RentalItemsRes

    /* An API-method that fetches a rental item according to the rental item
    id from http://10.0.2.2:8000/api/v1/rentalitem/{rental_item_id}. It
    returns a RentalItemRes data class object.
    */
    @GET("rentalitem/{rentalItemId}/")
    suspend fun getRentalItemById(@Path("rentalItemId") rentalItemId: Int): RentalItemRes

    /* An API-method that modifies the name value of a rental item according
    to the rental item id. The rental item resource is applied from
    http://10.0.2.2:8000/api/v1/rentalitem/{rental_item_id}. It returns a
    RentalItemRes data class object with the new value set by reqBody parameter.
    */
    @PUT("rentalitem/{rentalItemId}/")
    suspend fun editRentalItem(
        @Path("rentalItemId") rentalItemId: Int,
        @Body reqBody: UpdateRentalItemReq
    ): RentalItemRes

    @DELETE("rentalitem/{rentalItemId}/")
    suspend fun removeRentalItem(
        @Path("rentalItemId") rentalItemId: Int
    )
}