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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.inventorymanagementapplication.viewmodel.RegistrationViewModel


/* Builds the top bar with a back arrow icon. When clicked, the callback
function provided as an argument (navController.navigateUp()) is called.

Builds the UI for the registration screen using the RegistrationScreen
composable. Displays a CircularProgressIndicator if the loading argument of
the registrationState is true. Otherwise displays two text fields and a
Registration button. The text in the last text field is masked for security.
The button can be pressed when there's text in both fields.

When the button is pressed, the callback function is called.
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(goBack: () -> Unit, onRegistrationClick: () -> Unit) {
    val registrationVM: RegistrationViewModel = viewModel()
    val context = LocalContext.current

    /* If registration is not successful, the error of the registration
    state won't be null and the error message is shown in a Toast.
    */
    LaunchedEffect(key1 = registrationVM.registrationState.value.error) {
        // if error != null:
        registrationVM.registrationState.value.error?.let {
            Toast.makeText(
                context, registrationVM.registrationState.value.error, Toast.LENGTH_LONG
            ).show()
            registrationVM.clearRegistrationError()
        }
    }

    /* If registration is successful, onRegistrationClick callback is called,
    and the navigation to LoginScreen is implemented (which is defined in
    MainActivity).
    */
    LaunchedEffect(key1 = registrationVM.registrationState.value.registrationOk) {
        if (registrationVM.registrationState.value.registrationOk) {
            registrationVM.setRegistration(ok = false)
            onRegistrationClick()
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = { goBack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.registration)
                    )
                }
            },
            title = { Text(text = stringResource(id = R.string.register)) }
        )
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when {
                registrationVM.registrationState.value.loading -> CircularProgressIndicator(
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
                        value = registrationVM.registrationState.value.username,
                        onValueChange = {
                            registrationVM.setUsername(it)
                        },
                        placeholder = {
                            Text(text = stringResource(id = R.string.username))
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        visualTransformation = PasswordVisualTransformation(),
                        value = registrationVM.registrationState.value.password,
                        onValueChange = {
                            registrationVM.setPassword(it)
                        },
                        placeholder = {
                            Text(text = stringResource(id = R.string.password))
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        enabled = registrationVM.registrationState.value.username != "" &&
                                registrationVM.registrationState.value.password != "",
                        onClick = {
                            registrationVM.register()
                        }) {
                        Text(text = stringResource(id = R.string.register))
                    }
                }
            }
        }
    }
}