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
import com.example.inventorymanagementapplication.viewmodel.CategoryEditViewModel


/* Builds the top bar and the UI for editing a category using the
CategoryEditScreen composable function. Displays a CircularProgressIndicator
if the loading argument of the categoryState is true. Alternatively if an
error occurs, an error message is displayed on the screen.

Otherwise, displays a text field and two adjacent buttons below the field.
When the Edit button is pressed, the editCategories method of the vm instance
is called. When the Back button is pressed, the goBack callback is called.
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryEditScreen(goToCategories: () -> Unit, goBack: () -> Unit) {
    // An instance of the CategoryEditViewModel is created:
    val vm: CategoryEditViewModel = viewModel()

    /* The lambda block of LaunchedEffect is executed whenever the value
    of the key1 parameter changes. Then it is checked if the value of the
    done argument of the vm instance's state is true. If so, the value
    is set back to false and, as a side effect, the callback method
    transmitted as a parameter is called.
    */
    LaunchedEffect(key1 = vm.categoryState.value.done) {
        if (vm.categoryState.value.done) {
            vm.setDone(done = false)
            goToCategories()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.edit_category_name))
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when {
                vm.categoryState.value.loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                vm.categoryState.value.error != null -> Text(
                    text = stringResource(id = R.string.error_with_colon) +
                            " ${vm.categoryState.value.error.toString()}"
                )

                else -> Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = vm.categoryState.value.categoryName,
                        onValueChange = { newName -> vm.setName(newName) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Button(onClick = { goBack() }) {
                            Text(text = stringResource(id = R.string.cancel))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { vm.editCategory() }) {
                            Text(text = stringResource(id = R.string.edit))
                        }
                    }
                }
            }
        }
    }
}