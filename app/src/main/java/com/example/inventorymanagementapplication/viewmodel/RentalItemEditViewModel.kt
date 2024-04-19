package com.example.inventorymanagementapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapplication.api.categoriesService
import com.example.inventorymanagementapplication.api.rentalItemsService
import com.example.inventorymanagementapplication.model.RentalItemState
import com.example.inventorymanagementapplication.model.UpdateRentalItemReq
import kotlinx.coroutines.launch

// This view model is used for controlling data related modifying a rental item.
class RentalItemEditViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    /* The private attribute of the class representing the current rental item
    state relating to RentalItemState data class which includes rentalItemName,
    loading, done and error attributes:
    */
    private val _rentalItemState = mutableStateOf(RentalItemState())

    /* The public non-mutable variable representing the current rental item state
    providing read-only access:
    */
    val rentalItemState: State<RentalItemState> = _rentalItemState

    /* The value of the id variable is set to the rental item id obtained from
    savedStateHandle transmitted as a parameter. If there's no value or it
    cannot be converted to integer, id is set to 0.
    */
    private val rentalItemId = savedStateHandle.get<String>("rentalItemId")?.toIntOrNull() ?: 0

    private val categoryId = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0

    /* The method inside init lambda is called immediately when an instance
    of this class is created:
    */
    init {
        getRentalItemById()
    }


    /* A function for fetching a rental item by id from the API and updating
    the rental item state accordingly. viewModelScope is used for coroutine
    scope since the API call is asynchronous. The value of the loading
    argument of the RentalItemState's instance is set from false to true.
    getRentalItemById (API) interface method is then called to get the rental
    item response. The rental item state is updated with the rentalItemName
    argument of the response. If any error occurs, the error state is set.
    Finally the loading state is set back to false.
    */
    private fun getRentalItemById() {
        try {
            viewModelScope.launch {
                _rentalItemState.value = _rentalItemState.value.copy(loading = true)
                val response = rentalItemsService.getRentalItemById(rentalItemId)
                _rentalItemState.value =
                    _rentalItemState.value.copy(rentalItemName = response.rentalItemName)
            }
        } catch (e: Exception) {
            _rentalItemState.value = _rentalItemState.value.copy(error = e.toString())
        } finally {
            _rentalItemState.value = _rentalItemState.value.copy(loading = false)
        }
    }


    /* A public setter method for updating the rentalItemName argument of the
    _rentalItemState's RentalItemState instance:
     */
    fun setName(newName: String) {
        _rentalItemState.value = _rentalItemState.value.copy(rentalItemName = newName)
    }


    /* A public setter method for updating the done argument of the
    _rentalItemState's CategoryState instance:
    */
    fun setDone(done: Boolean) {
        _rentalItemState.value = _rentalItemState.value.copy(done = done)
    }


    /* A method for fetching a rental item by id from the API and updating the
    rental item name using the editRentalItem (API) interface method. The id
    and the rentalItemName arguments are given as parameters to the
    editRentalItem method. The value of the rentalItemName parameter is set
    to the rentalItemName value of the rental item state's instance.
    _rentalItemState's done argument is set to true which activates the
    LaunchedEffect lambda in RentalItemEditScreen and fulfils the condition so
    that the navigation to the RentalItemScreen is implemented.
    */
    fun editRentalItem() {
        viewModelScope.launch {
            try {
                _rentalItemState.value = _rentalItemState.value.copy(loading = true)
                _rentalItemState.value = _rentalItemState.value.copy(categoryId = categoryId)
                println("KATEGORIA EDIT! $categoryId")
                rentalItemsService.editRentalItem(
                    rentalItemId,
                    UpdateRentalItemReq(rentalItemName = _rentalItemState.value.rentalItemName)
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