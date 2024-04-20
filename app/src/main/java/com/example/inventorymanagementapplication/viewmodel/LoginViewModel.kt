package com.example.inventorymanagementapplication.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapplication.AccountDatabase
import com.example.inventorymanagementapplication.AccountEntity
import com.example.inventorymanagementapplication.DbProvider
import com.example.inventorymanagementapplication.api.authorizationService
import com.example.inventorymanagementapplication.model.AuthReq
import com.example.inventorymanagementapplication.model.LoginState
import kotlinx.coroutines.launch

// Connecting View i.e. UI and Model i.e. data (class).
class LoginViewModel(private val db: AccountDatabase = DbProvider.db) : ViewModel() {
    // The private attribute of the class representing the current login state:
    private val _loginState: MutableState<LoginState> = mutableStateOf(LoginState())

    /* The public non-mutable variable representing the current login state
    providing read-only access to the login state:
     */
    val loginState: State<LoginState> = _loginState

    init {
        viewModelScope.launch {
            try {
                val accessToken = db.accountDao().getToken()
                accessToken?.let {
                    val res = authorizationService.getAccount(bearerToken = "Bearer $it")
                    _loginState.value = _loginState.value.copy(accountId = res.authUserId)
                }
            } catch (e: Exception) {
                _loginState.value = _loginState.value.copy(error = e.toString())
            }
        }
    }

    fun setAccountId(id: Int) {
        _loginState.value = _loginState.value.copy(accountId = id)
    }


    // Public setter methods for updating values of the login state:
    fun setUsername(newUsername: String) {
        _loginState.value = _loginState.value.copy(username = newUsername)
    }

    fun setPassword(newPassword: String) {
        _loginState.value = _loginState.value.copy(password = newPassword)
    }

    fun setLogin(ok: Boolean) {
        _loginState.value = _loginState.value.copy(loginOk = ok)
    }

    /* A method that handles the asynchronous login process and updates the
    loading status in the login state:
    */
    fun login() {
        viewModelScope.launch {
            try {
                _loginState.value = _loginState.value.copy(loading = true)
                val response = authorizationService.login(
                    req = AuthReq(
                        username = _loginState.value.username,
                        password = _loginState.value.password
                    )
                )
                // Access token is saved into the room database:
                db.accountDao().addToken(
                    entity = AccountEntity(accessToken = response.accessToken)
                )
                setLogin(ok = true)
            } catch (e: Exception) {
                _loginState.value = _loginState.value.copy(error = e.toString())
            } finally {
                _loginState.value = _loginState.value.copy(loading = false)
            }
        }
    }
}