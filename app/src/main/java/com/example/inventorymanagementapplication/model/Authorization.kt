package com.example.inventorymanagementapplication.model

import com.google.gson.annotations.SerializedName

data class RegistrationState(
    val username: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val registrationOk: Boolean = false
)

data class AuthReq(
    val username: String = "",
    val password: String = ""
)


data class LoginState(
    val username: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val loginOk: Boolean = false,
    val accountId: Int = 0
)

data class AuthRes(
    @SerializedName("access_token")
    val accessToken: String = ""
)

data class LogoutState(
    val loading: Boolean = false,
    val error: String? = null,
    val logoutOk: Boolean = false
)

data class AccountRes(
    @SerializedName("auth_user_id")
    val authUserId: Int = 0
)