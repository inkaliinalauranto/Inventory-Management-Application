package com.example.inventorymanagementapplication.api

import com.example.inventorymanagementapplication.model.AddRentalItemReq
import com.example.inventorymanagementapplication.model.RentalItemRes
import com.example.inventorymanagementapplication.model.AddRentalItemRes
import com.example.inventorymanagementapplication.model.RentalItemsRes
import com.example.inventorymanagementapplication.model.UpdateRentalItemReq
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/* A retrofit service interface implementation is created from RentalItemsApi
interface. The service implements the methods of the interface:
*/
val rentalItemsService: RentalItemsApi = retrofit.create(RentalItemsApi::class.java)

interface RentalItemsApi {
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

    /* An API-method that adds a new rental item which is based on the data
    transmitted in the AddRentalItemReq-shaped data class. The rental item is
    added to the category specified as a path parameter. This method returns
    data in the format of the AddRentalItemRes data class.
    */
    @POST("category/{categoryId}/items/")
    suspend fun addRentalItem(
        @Path("categoryId") categoryId: Int,
        @Body reqBody: AddRentalItemReq
    ): AddRentalItemRes


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

    /* An API-method that removes a rental item according to the rental item
    id. The rental item resource is retrieved from
    http://10.0.2.2:8000/api/v1/rentalitem/{rentalItemId}.
    */
    @DELETE("rentalitem/{rentalItemId}/")
    suspend fun removeRentalItem(
        @Path("rentalItemId") rentalItemId: Int
    )
}