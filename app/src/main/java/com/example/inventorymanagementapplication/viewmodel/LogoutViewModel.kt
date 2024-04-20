package com.example.inventorymanagementapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapplication.DbProvider.db
import com.example.inventorymanagementapplication.api.authorizationService
import com.example.inventorymanagementapplication.model.LogoutState
import kotlinx.coroutines.launch

class LogoutViewModel() : ViewModel() {
    /* The private attribute of the class representing the current logout
    state:
    */
    private val _logoutState = mutableStateOf(LogoutState())

    /* The public non-mutable variable representing the current logout state
    providing read-only access:
    */
    val logoutState: State<LogoutState> = _logoutState


    // Public setter method for updating the logoutOk value of the login state:
    fun setLogout(ok: Boolean) {
        _logoutState.value = _logoutState.value.copy(logoutOk = ok)
    }


    /* A method that handles the asynchronous logout process and updates the
    loading status in the logout state:
    */
    fun logout() {
        viewModelScope.launch {
            try {
                _logoutState.value = _logoutState.value.copy(loading = true)
                val accessToken = db.accountDao().getToken()
                // If accessToken != null
                accessToken?.let {
                    authorizationService.logout(bearerToken = "Bearer $it")
                    db.accountDao().removeTokens()
                    setLogout(ok = true)
                }
            } catch (e: Exception) {
                _logoutState.value = _logoutState.value.copy(error = e.toString())
            } finally {
                _logoutState.value = _logoutState.value.copy(loading = false)
            }
        }
    }
}