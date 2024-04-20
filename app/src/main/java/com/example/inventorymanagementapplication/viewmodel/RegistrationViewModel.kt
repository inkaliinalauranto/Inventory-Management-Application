package com.example.inventorymanagementapplication.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapplication.api.authorizationService
import com.example.inventorymanagementapplication.model.AuthReq
import com.example.inventorymanagementapplication.model.RegistrationState
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {
    /* The private attribute of the class representing the current
    registration state:
    */
    private val _registrationState: MutableState<RegistrationState> =
        mutableStateOf(RegistrationState())

    /* The public non-mutable variable representing the current registration
    state providing read-only access:
    */
    val registrationState: State<RegistrationState> = _registrationState


    // Public setter methods for updating values of the registration state:

    fun setUsername(newUsername: String) {
        _registrationState.value = _registrationState.value.copy(username = newUsername)
    }


    fun setPassword(newPassword: String) {
        _registrationState.value = _registrationState.value.copy(password = newPassword)
    }


    fun setRegistration(ok: Boolean) {
        _registrationState.value = _registrationState.value.copy(registrationOk = ok)
    }


    /* A method that handles the asynchronous registration process and
    updates the loading status in the registration state:
    */
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
                /* If authorizationService.register function call is
                successful, the code continues in the try branch and the
                value of the registrationOk property of the registration
                state is se to true:
                */
                setRegistration(ok = true)
            } catch (e: Exception) {
                _registrationState.value = _registrationState.value.copy(error = e.toString())
            } finally {
                _registrationState.value = _registrationState.value.copy(loading = false)
            }
        }
    }


    // Resets the error state:
    fun clearRegistrationError() {
        _registrationState.value = _registrationState.value.copy(error = null)
    }
}