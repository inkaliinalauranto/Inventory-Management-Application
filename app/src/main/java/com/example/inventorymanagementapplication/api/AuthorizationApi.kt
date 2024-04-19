package com.example.inventorymanagementapplication.api

import com.example.inventorymanagementapplication.model.AccountRes
import com.example.inventorymanagementapplication.model.AuthReq
import com.example.inventorymanagementapplication.model.AuthRes
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/* A retrofit service interface implementation is created from LoginApi
interface. The service implements the methods of the interface:
*/
val authorizationService: AuthorizationApi = retrofit.create(AuthorizationApi::class.java)

interface AuthorizationApi {
    @POST("auth/register")
    suspend fun register(@Body req: AuthReq)

    @POST("auth/login")
    suspend fun login(@Body req: AuthReq): AuthRes
    
    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") bearerToken: String)

    @GET("auth/account")
    suspend fun getAccount(@Header("Authorization") bearerToken: String): AccountRes
}