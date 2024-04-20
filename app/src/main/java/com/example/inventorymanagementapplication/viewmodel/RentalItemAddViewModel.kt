package com.example.inventorymanagementapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapplication.DbProvider
import com.example.inventorymanagementapplication.api.authorizationService
import com.example.inventorymanagementapplication.api.rentalItemsService
import com.example.inventorymanagementapplication.model.AddRentalItemReq
import com.example.inventorymanagementapplication.model.RentalItemState
import kotlinx.coroutines.launch

class RentalItemAddViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    /* The private attribute of the class representing the current rental
    item state relating to RentalItemState data class which includes
    categoryId, rentalItemName, loading, error and done attributes:
    */
    private val _rentalItemState = mutableStateOf(RentalItemState())

    /* The public non-mutable variable representing the current rental item
    state providing read-only access:
    */
    val rentalItemState: State<RentalItemState> = _rentalItemState

    /* The value of the categoryId variable is set to the category id obtained
    from savedStateHandle transmitted as a parameter. If there's no value or it
    cannot be converted to integer, categoryId is set to 0.
    */
    private val categoryId = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0


    // Public setter methods for updating values of the rental item state:

    fun setName(newName: String) {
        _rentalItemState.value = _rentalItemState.value.copy(rentalItemName = newName)
    }


    fun setDone(done: Boolean) {
        _rentalItemState.value = _rentalItemState.value.copy(done = done)
    }


    /* A method for fetching the authorized user by access token from the API
    and then adding a rental item using the the addRentalItem (API) interface
    method. The rental item name and the id of the authorized user are given
    as parameters. If these methods are successful and don't cause any errors,
    _rentalItemState's done argument is set to true which activates the
    LaunchedEffect lambda in RentalItemAddScreen and fulfils the condition so
    that the navigation to RentalItemsScreen is implemented.
    */
    fun addRentalItem() {
        viewModelScope.launch {
            try {
                _rentalItemState.value = _rentalItemState.value.copy(
                    loading = true,
                    categoryId = categoryId
                )
                val accessToken = DbProvider.db.accountDao().getToken()
                val accountResponse = authorizationService.getAccount(bearerToken = "Bearer $accessToken")

                rentalItemsService.addRentalItem(
                    categoryId = categoryId,
                    AddRentalItemReq(
                        rentalItemName = _rentalItemState.value.rentalItemName,
                        createdByUserId = accountResponse.authUserId
                    )
                )

                setDone(done = true)
            } catch (e: Exception) {
                _rentalItemState.value = _rentalItemState.value.copy(error = e.toString())
            } finally {
                _rentalItemState.value = _rentalItemState.value.copy(loading = false)
            }
        }
    }
}