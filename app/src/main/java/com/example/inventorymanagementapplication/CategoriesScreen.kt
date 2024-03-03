package com.example.inventorymanagementapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventorymanagementapplication.viewmodel.CategoriesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(onMenuClicked: () -> Unit) {
    // An instance of the CategoriesViewModel is created:
    val categoriesVM: CategoriesViewModel = viewModel()
    // Top bar is created insides Scaffold:
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Categories") },
            navigationIcon = {
                // Menu icon button:
                IconButton(onClick = {
                    onMenuClicked()
                }) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                }
            }
        )
    }) {
        /* The actual content of the screen is created as an implementation
        of the Box composable. When fetching categories, the circular progress
        indicator is shown. Alternatively if any error occurs, an error
        message is displayed on the screen. Otherwise, category items are
        displayed on the screen.
        */
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when {
                categoriesVM.categoriesState.value.loading -> CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )
                categoriesVM.categoriesState.value.error != null -> Text(
                    text = "Error: ${categoriesVM.categoriesState.value.error}"
                )
                else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // A single item of items is referenced as "it":
                    items(categoriesVM.categoriesState.value.list) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Kuva tähän")
                                Text(text = it.categoryName, style = MaterialTheme.typography.headlineLarge)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                /* When the delete icon button is clicked, the
                                category item related to the icon button is
                                deleted:
                                 */
                                IconButton(onClick = {
                                    categoriesVM.deleteCategory(it)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete, contentDescription = "Delete"
                                    )
                                }
                                IconButton(onClick = {
                                    // goToCategoryEdit(it)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit, contentDescription = "Edit"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
