package com.example.inventorymanagementapplication

import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventorymanagementapplication.viewmodel.CategoriesViewModel
import com.example.inventorymanagementapplication.viewmodel.ItemsViewModel


/* Parameters:
- loading: a boolean of which value defines if the confirm button is enabled.
- error: a nullable string.
- onDismiss: a callback function invoked when the dismiss text button is
  clicked.
- onConfirm: a callback function invoked when the confirm text button is
  clicked.
- clearError: a callback function invoked when the value of the error
  parameter changes from null to a string.
 */
@Composable
fun ConfirmItemDelete(
    loading: Boolean,
    error: String?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    clearError: () -> Unit
) {
    val context: Context = LocalContext.current

    /* The lambda block of LaunchedEffect is executed whenever the value
    of the key1 parameter changes. The lambda of error?.let is executed
    whenever the value of nullable string shaped error variable is a string.
    In that case, the value of the error parameter is printed as a Toast
    message. clearError callback function entered as a parameter switches
    the value of the categoryDeleteState object's attribute to null after
    displaying the toast.
    */
    LaunchedEffect(key1 = error) {
        error?.let {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            clearError()
        }
    }

    /* When a trash can icon of a single category item is pressed in the
    CategoriesScreen, this confirmation window view is displayed.
    */
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        confirmButton = {
            TextButton(
                onClick = { onConfirm() },
                enabled = !loading
            ) {
                Text(text = "Delete")
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete category"
            )
        },
        text = {
            Box(modifier = Modifier.fillMaxWidth()) {
                when {
                    loading -> CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )

                    else -> Text(text = "Are you sure you want to delete this category?")
                }
            }
        },
        title = { Text(text = "Delete category") },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Cancel")
            }
        }
    )
}


/* Parameters:
- onMenuClicked: Callback function invoked when the menu icon is clicked.
- goToCategoryEdit: Callback function invoked to navigate to the
CategoryEditScreen with a CategoryItem parameter.
- goToCategoryAdd: When the floating button is clicked, the navigation to
the CategoryAddScreen is implemented through this callback.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(
//    onMenuClicked: () -> Unit,
//    goToCategoryEdit: (CategoryItem) -> Unit,
//    goToCategoryAdd: () -> Unit
) {
    // An instance of the CategoriesViewModel is created:
    val itemsVM: ItemsViewModel = viewModel()
    // Top bar is created insides Scaffold:
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /*goToCategoryAdd()*/ }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add category")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Categories") },
                navigationIcon = {
                    // Menu icon button:
                    IconButton(onClick = {
                        /*onMenuClicked()*/
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
                itemsVM.itemsState.value.loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                itemsVM.itemsState.value.error != null -> Text(
                    text = "Error: ${itemsVM.itemsState.value.error}"
                )

                /* When the trash can icon of a category is clicked, the
                id argument of the CategoryDeleteState is set to the id of
                the clicked category item. In this case, the id is always
                greater than 0, and the ConfirmCategoryDelete confirmation
                window is displayed.
                */
                itemsVM.itemDeleteState.value.id > 0 -> ConfirmCategoryDelete(
                    loading = itemsVM.itemDeleteState.value.loading,
                    error = itemsVM.itemDeleteState.value.error,
                    onDismiss = { /*itemsVM.setDeletableCategoryId(id = 0)*/ },
                    onConfirm = { /*itemsVM.deleteCategory()*/ },
                    clearError = { /*itemsVM.clearDeleteError()*/ }
                )

                else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // A single category item is referenced as "it":
                    items(itemsVM.itemsState.value.list) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // A picture is retrieved:
                                ItemImage()
                                Text(
                                    text = it.itemName,
                                    style = MaterialTheme.typography.headlineLarge
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                /* When a category's Delete icon button is
                                clicked, the category id of the clicked
                                category is set into _categoryDeleteState of
                                the categoriesVM instance.
                                 */
                                IconButton(onClick = {
                                    /*itemsVM.setDeletableCategoryId(it.categoryId)*/
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete"
                                    )
                                }
                                IconButton(onClick = {
                                    /*goToCategoryEdit(it)*/
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit"
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
