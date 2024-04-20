package com.example.inventorymanagementapplication.view

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.inventorymanagementapplication.R
import com.example.inventorymanagementapplication.model.CategoryItem
import com.example.inventorymanagementapplication.viewmodel.CategoriesViewModel
import java.time.LocalDateTime


/* A composable function that asynchronously loads and displays an image from
a URL address.
*/
@Composable
fun ItemImage() {
    AsyncImage(
        model = "https://picsum.photos/seed/${LocalDateTime.now()}/200",
        contentDescription = null
    )
}

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
fun ConfirmCategoryDelete(
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
                Text(text = stringResource(id = R.string.delete))
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(id = R.string.delete_category)
            )
        },
        text = {
            Box(modifier = Modifier.fillMaxWidth()) {
                when {
                    loading -> CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )

                    else -> Text(text = stringResource(id = R.string.delete_category_confirmation_text))
                }
            }
        },
        title = { Text(text = stringResource(id = R.string.delete_category)) },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = stringResource(id = R.string.cancel))
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
fun CategoriesScreen(
    onMenuClicked: () -> Unit,
    goToCategoryEdit: (CategoryItem) -> Unit,
    goToCategoryAdd: () -> Unit,
    goToRentalItemList: (CategoryItem) -> Unit
) {
    // An instance of the CategoriesViewModel is created:
    val categoriesVM: CategoriesViewModel = viewModel()
    // Top bar is created insides Scaffold:
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { goToCategoryAdd() }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_category)
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.categories)) },
                navigationIcon = {
                    // Menu icon button:
                    IconButton(onClick = {
                        onMenuClicked()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(id = R.string.menu)
                        )
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
                    modifier = Modifier.align(Alignment.Center)
                )

                categoriesVM.categoriesState.value.error != null -> Text(
                    text = stringResource(id = R.string.error_with_colon) + " ${categoriesVM.categoriesState.value.error}"
                )

                /* When the trash can icon of a category is clicked, the
                id argument of the CategoryDeleteState is set to the id of
                the clicked category item. In this case, the id is always
                greater than 0, and the ConfirmCategoryDelete confirmation
                window is displayed.
                */
                categoriesVM.categoryDeleteState.value.id > 0 -> ConfirmCategoryDelete(
                    loading = categoriesVM.categoryDeleteState.value.loading,
                    error = categoriesVM.categoryDeleteState.value.error,
                    onDismiss = { categoriesVM.setDeletableCategoryId(id = 0) },
                    onConfirm = { categoriesVM.deleteCategory() },
                    clearError = { categoriesVM.clearDeleteError() }
                )

                else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // A single category item is referenced as "it":
                    items(categoriesVM.categoriesState.value.list) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 25.dp, top = 25.dp)
                            ) {
                                Column(
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                ) {
                                    // A picture is retrieved:
                                    ItemImage()
                                }
                                Column(
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = it.categoryName,
                                            style = MaterialTheme.typography.headlineLarge
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        TextButton(onClick = { goToRentalItemList(it) }) {
                                            Text(text = stringResource(id = R.string.show_rental_items))
                                        }
                                        IconButton(onClick = {
                                            goToCategoryEdit(it)
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = stringResource(id = R.string.edit)
                                            )
                                        }
                                        /* When a category's Delete icon button is
                                        clicked, the category id of the clicked
                                        category is set into _categoryDeleteState of
                                        the categoriesVM instance.
                                         */
                                        IconButton(onClick = {
                                            categoriesVM.setDeletableCategoryId(it.categoryId)
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = stringResource(id = R.string.delete)
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
    }
}
