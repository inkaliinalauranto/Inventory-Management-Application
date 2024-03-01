package com.example.inventorymanagementapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventorymanagementapplication.viewmodel.LoginViewModel


/* Builds the UI for the login screen using the LoginScreen composable
function. Displays a CircularProgressIndicator if the loading argument of
the loginState is true. Otherwise displays two text fields and a Login
button. The text in the last text field is masked for security. The button
can be pressed when there's text in both fields.
*/
@Preview
@Composable
fun LoginScreen() {
    val loginVM: LoginViewModel = viewModel()
    Box(modifier = Modifier.fillMaxSize()) {

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
                        Text(text = "Username")
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
                        Text(text = "Password")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    enabled = loginVM.loginState.value.username != "" && loginVM.loginState.value.password != "",
                    onClick = {
                        loginVM.login()
                    }) {
                    Text(text = "Login")
                }
            }
        }
    }
}