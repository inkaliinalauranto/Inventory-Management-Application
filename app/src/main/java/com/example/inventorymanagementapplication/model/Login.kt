package com.example.inventorymanagementapplication.model

import com.google.gson.annotations.SerializedName

// The data i.e. the model part of MVVM:
data class LoginState(
    val username: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val loginOk: Boolean = false,
    val accountId: Int = 0
)

data class AuthReq(
    val username: String = "",
    val password: String = ""
)

data class AuthRes(
    @SerializedName("access_token")
    val accessToken: String = ""
)

data class AccountRes(
    @SerializedName("auth_user_id")
    val authUserId: Int = 0
)

data class LogoutState(
    val loading: Boolean = false,
    val error: String? = null,
    val logoutOk: Boolean = false
)