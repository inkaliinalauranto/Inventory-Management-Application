package com.example.inventorymanagementapplication.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventorymanagementapplication.R
import com.example.inventorymanagementapplication.viewmodel.LoginViewModel


/* Builds the UI for the login screen using the LoginScreen composable
function. Displays a CircularProgressIndicator if the loading argument of
the loginState is true. Otherwise displays two text fields and a Login
button. The text in the last text field is masked for security. The button
can be pressed when there's text in both fields.

When the button is pressed, its callback function is called. If the
registration text button is pressed, its callback function is called.
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    goToCategories: () -> Unit,
    onLoginClick: () -> Unit,
    goToRegistrationScreen: () -> Unit
) {
    val loginVM: LoginViewModel = viewModel()
    val context = LocalContext.current

    /* If login is not successful, the error of the login state won't be
    null and the error message is shown in a Toast.
     */
    LaunchedEffect(key1 = loginVM.loginState.value.error) {
        loginVM.loginState.value.error?.let {
            Toast.makeText(context, loginVM.loginState.value.error, Toast.LENGTH_LONG).show()
            loginVM.clearLoginError()
        }
    }

    /* If login is successful, onLoginClick callback is called, and the
    navigation to the CategoriesScreen is implemented (which is defined
    in the MainActivity).
    */
    LaunchedEffect(key1 = loginVM.loginState.value.loginOk) {
        if (loginVM.loginState.value.loginOk) {
            loginVM.setLogin(ok = false)
            onLoginClick()
        }
    }

    /* If the accountId already exists in the database, i.e. the user is
    logged in, the navigation to the CategoriesScreen is executed straight
    away.
    */
    LaunchedEffect(key1 = loginVM.loginState.value.accountId) {
        if (loginVM.loginState.value.accountId > 0) {
            loginVM.setAccountId(id = 0)
            goToCategories()
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            navigationIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = stringResource(id = R.string.login))
            },
            title = { Text(text = stringResource(id = R.string.login)) }
        )
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when {
                loginVM.loginState.value.loading -> CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )

                else -> Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = loginVM.loginState.value.username,
                        onValueChange = {
                            loginVM.setUsername(it)
                        },
                        placeholder = {
                            Text(text = stringResource(id = R.string.username))
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        visualTransformation = PasswordVisualTransformation(),
                        value = loginVM.loginState.value.password,
                        onValueChange = {
                            loginVM.setPassword(it)
                        },
                        placeholder = {
                            Text(text = stringResource(id = R.string.password))
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        enabled = loginVM.loginState.value.username != "" &&
                                loginVM.loginState.value.password != "",
                        onClick = {
                            loginVM.login()
                        }) {
                        Text(text = stringResource(id = R.string.login))
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = stringResource(id = R.string.no_account_text))
                    TextButton(onClick = { goToRegistrationScreen() }) {
                        Text(text = stringResource(id = R.string.register_here))
                    }
                }
            }
        }
    }
}