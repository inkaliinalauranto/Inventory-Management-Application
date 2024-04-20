package com.example.inventorymanagementapplication.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventorymanagementapplication.R
import com.example.inventorymanagementapplication.viewmodel.CategoryAddViewModel

/* Builds the top bar and the UI for adding a category using the
CategoryAddScreen composable function. Displays a CircularProgressIndicator
if the loading argument of the categoryState is true. Alternatively if an
error occurs, an error message is displayed on the screen.

Otherwise, it displays a text field and two adjacent buttons below the field.
When the add button is pressed, the addCategories method of the vm instance
is called. When the Back button is pressed, the goBack callback is called.
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryAddScreen(goToCategories: () -> Unit, goBack: () -> Unit) {
    val vm: CategoryAddViewModel = viewModel()

    /* The lambda block of LaunchedEffect is executed whenever the value
    of the key1 parameter changes. If the value of the done argument of the
    vm instance's state is true, goToCategories callback is called for
    navigating back to CategoriesScreen composable. Also the done argument
    is set back to false so that it doesn't stay the same during the
    execution of the program blocking the implementation of the navigation
    logic.
    */
    LaunchedEffect(key1 = vm.categoryState.value.done) {
        if (vm.categoryState.value.done) {
            vm.setDone(false)
            goToCategories()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.add_category))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        goBack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                }
            )
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            when {
                vm.categoryState.value.loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                vm.categoryState.value.error != null -> Text(text = stringResource(id = R.string.error_with_colon) + " ${vm.categoryState.value.error}")

                else -> Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = vm.categoryState.value.categoryName,
                        onValueChange = { name -> vm.setName(name) }
                    )
                    Spacer(modifier = Modifier.height(height = 16.dp))
                    Row {
                        Button(onClick = { goBack() }) {
                            Text(text = stringResource(id = R.string.cancel))
                        }
                        Spacer(modifier = Modifier.width(width = 8.dp))
                        Button(onClick = { vm.addCategory() }) {
                            Text(text = stringResource(id = R.string.add))
                        }
                    }
                }
            }
        }
    }
}