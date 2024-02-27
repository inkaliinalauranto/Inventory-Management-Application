package com.example.inventorymanagementapplication.model

// The data i.e. the model part of MVVM:
data class LoginState(
    val username: String = "",
    val password: String = "",
    val loading: Boolean = false
)