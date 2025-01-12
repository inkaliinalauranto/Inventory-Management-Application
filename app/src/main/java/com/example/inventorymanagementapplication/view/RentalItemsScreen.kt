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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.example.inventorymanagementapplication.R
import com.example.inventorymanagementapplication.model.RentalItemCategoryState
import com.example.inventorymanagementapplication.model.RentalItemsState
import com.example.inventorymanagementapplication.viewmodel.RentalItemsViewModel


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
fun ConfirmRentalItemDelete(
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
    the value of the rentalItemDeleteState object's attribute to null after
    displaying the toast.
    */
    LaunchedEffect(key1 = error) {
        error?.let {
            println(error)
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            clearError()
        }
    }

    /* When a trash can icon of a single rental item is pressed in the
    RentalItemsScreen, this confirmation window view is displayed.
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
                contentDescription = stringResource(id = R.string.delete_rental_item)
            )
        },
        text = {
            Box(modifier = Modifier.fillMaxWidth()) {
                when {
                    loading -> CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )

                    else -> Text(
                        text = stringResource(id = R.string.delete_rental_item_confirmation_text)
                    )
                }
            }
        },
        title = { Text(text = stringResource(id = R.string.delete_rental_item)) },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}


/* Parameters:
- goToRentalItemEdit: Callback function invoked to navigate to the
RentalItemEditScreen with a RentalItemCategoryState parameter.
- goToRentalItemAdd: When the floating button is clicked, the navigation to
the RentalItemAddScreen is implemented through this callback.
- goBack: Callback function invoked when the back arrow icon is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentalItemsScreen(
    goToRentalItemEdit: (RentalItemCategoryState) -> Unit,
    goToRentalItemAdd: (RentalItemsState) -> Unit,
    goBack: () -> Unit
) {
    // An instance of the RentalItemsViewModel is created:
    val rentalItemsVM: RentalItemsViewModel = viewModel()


    // Top bar is created insides Scaffold:
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { goToRentalItemAdd(rentalItemsVM.rentalItemsState.value) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add, 
                    contentDescription = stringResource(id = R.string.add_rental_item)
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.rental_items)) },
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
        }) { it ->
        /* The actual content of the screen is created as an implementation
        of the Box composable. When fetching rental items, the circular
        progress indicator is shown. Alternatively if any error occurs, an
        error message is displayed on the screen. Otherwise, rental items are
        displayed on the screen.
        */
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when {
                rentalItemsVM.rentalItemsState.value.loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                rentalItemsVM.rentalItemsState.value.error != null -> Text(
                    text = stringResource(id = R.string.error_with_colon) + " ${rentalItemsVM.rentalItemsState.value.error}"
                )

                /* When the trash can icon of a rental item is clicked, the
                id argument of the RentalItemDeleteState is set to the id of
                the clicked rental item. In this case, the id is always
                greater than 0, and the ConfirmRentalItemDelete confirmation
                window is displayed.
                */
                rentalItemsVM.rentalItemDeleteState.value.id > 0 -> ConfirmRentalItemDelete(
                    loading = rentalItemsVM.rentalItemDeleteState.value.loading,
                    error = rentalItemsVM.rentalItemDeleteState.value.error,
                    onDismiss = { rentalItemsVM.setDeletableRentalItemId(id = 0) },
                    onConfirm = { rentalItemsVM.deleteRentalItem() },
                    clearError = { rentalItemsVM.clearDeleteError() }
                )

                else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // A single item is referenced as "it":
                    items(rentalItemsVM.rentalItemsState.value.list) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 25.dp, top = 25.dp)
                            ) {
                                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                                    // A picture is retrieved:
                                    ItemImage()
                                }
                                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = it.rentalItemName,
                                            style = MaterialTheme.typography.headlineLarge
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        IconButton(onClick = {
                                            rentalItemsVM.setRentalItemAndCategory(rentalItemId = it.rentalItemId)
                                            goToRentalItemEdit(rentalItemsVM.rentalItemCategoryState.value)
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = stringResource(id = R.string.edit)
                                            )
                                        }
                                        /* When a rental item's Delete icon
                                        button is clicked, the id of the
                                        clicked rental item is set into
                                        _rentalItemDeleteState of
                                        the rentalItemsVM instance.
                                         */
                                        IconButton(onClick = {
                                            rentalItemsVM.setDeletableRentalItemId(it.rentalItemId)
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
