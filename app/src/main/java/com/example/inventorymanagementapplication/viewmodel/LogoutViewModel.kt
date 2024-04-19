package com.example.inventorymanagementapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapplication.AccountDatabase
import com.example.inventorymanagementapplication.DbProvider
import com.example.inventorymanagementapplication.api.authorizationService
import com.example.inventorymanagementapplication.api.categoriesService
import com.example.inventorymanagementapplication.model.LogoutState
import kotlinx.coroutines.launch

class LogoutViewModel(private val db: AccountDatabase = DbProvider.db) : ViewModel() {
    private val _logoutState = mutableStateOf(LogoutState())
    val logoutState: State<LogoutState> = _logoutState

    fun setLogout(ok: Boolean) {
        _logoutState.value = _logoutState.value.copy(logoutOk = ok)
    }

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