package com.example.inventorymanagementapplication.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapplication.model.LoginState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Connecting View i.e. UI and Model i.e. data (class).
class LoginViewModel : ViewModel() {
    // The private attribute of the class representing the current login state:
    private var _loginState: MutableState<LoginState> = mutableStateOf(LoginState())

    /* The public non-mutable variable representing the current login state
    providing read-only access to the login state:
     */
    val loginState: State<LoginState> = _loginState

    // A Method that simulates an asynchronous delay for login purposes:
    private suspend fun _waitForLogin() {
        delay(2000)
    }

    // Public setter methods for updating values of the login state:
    fun setUsername(newUsername: String) {
        _loginState.value = _loginState.value.copy(username = newUsername)
    }

    fun setPassword(newPassword: String) {
        _loginState.value = _loginState.value.copy(password = newPassword)
    }

    /* A method that handles the asynchronous login process and updates the
    loading status in the login state:
    */
    fun login() {
        viewModelScope.launch {
            _loginState.value = _loginState.value.copy(loading = true)
            // Tähän oikeasti response:
            _waitForLogin()
            _loginState.value = _loginState.value.copy(loading = false)
        }
    }
}