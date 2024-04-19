package com.example.inventorymanagementapplication.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapplication.api.authorizationService
import com.example.inventorymanagementapplication.api.categoriesService
import com.example.inventorymanagementapplication.model.AuthReq
import com.example.inventorymanagementapplication.model.RegistrationState
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {
    private val _registrationState: MutableState<RegistrationState> =
        mutableStateOf(RegistrationState())
    val registrationState: State<RegistrationState> = _registrationState

    fun setUsername(newUsername: String) {
        _registrationState.value = _registrationState.value.copy(username = newUsername)
    }

    fun setPassword(newPassword: String) {
        _registrationState.value = _registrationState.value.copy(password = newPassword)
    }

    fun register() {
        viewModelScope.launch {
            try {
                _registrationState.value = _registrationState.value.copy(loading = true)
                authorizationService.register(
                    req = AuthReq(
                        username = _registrationState.value.username,
                        password = _registrationState.value.password
                    )
                )
            } catch (e: Exception) {
                _registrationState.value = _registrationState.value.copy(error = e.toString())
            } finally {
                _registrationState.value = _registrationState.value.copy(loading = false)
            }
        }
    }
}