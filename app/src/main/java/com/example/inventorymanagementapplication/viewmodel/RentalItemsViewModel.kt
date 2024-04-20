package com.example.inventorymanagementapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapplication.api.rentalItemsService
import com.example.inventorymanagementapplication.model.RentalItemCategoryState
import com.example.inventorymanagementapplication.model.RentalItemDeleteState
import com.example.inventorymanagementapplication.model.RentalItemsState
import kotlinx.coroutines.launch

class RentalItemsViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    /* The private attribute of the class representing the current rental
    items state:
    */
    private val _rentalItemsState = mutableStateOf(RentalItemsState())

    /* The public non-mutable variable representing the current rental
    items state providing read-only access:
    */
    val rentalItemsState: State<RentalItemsState> = _rentalItemsState

    /* The value of the categoryId variable is set to the category id obtained
    from savedStateHandle transmitted as a parameter. If there's no value or it
    cannot be converted to integer, categoryId is set to 0.
    */
    private val categoryId = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0

    /* The private attribute of the class representing the current rental
    item and its category. The RentalItemCategoryState data class includes
    categoryId and rentalItemId attributes:
    */
    private val _rentalItemCategoryState = mutableStateOf(RentalItemCategoryState())

    /* The public non-mutable variable representing the current rental
    item and category state providing read-only access. It is needed in
    RentalItemsScreen/MainActivity when navigating to RentalItemEditScreen.
    */
    val rentalItemCategoryState: State<RentalItemCategoryState> = _rentalItemCategoryState

    /* The private attribute of the class representing the current state of
    an instance created from RentalItemDeleteState data class.
    */
    private val _rentalItemDeleteState = mutableStateOf(RentalItemDeleteState())

    /* The public non-mutable variable representing the state of a rental item
     to be deleted providing read-only access to its state:
    */
    val rentalItemDeleteState: State<RentalItemDeleteState> = _rentalItemDeleteState


    /* When an instance is made from this class, the function inside
    init lambda is called:
    */
    init {
        getRentalItems()
    }


    /* A function for fetching rental items from the API and updating the
    rental items state accordingly. viewModelScope is used for coroutine scope
    since the API call is asynchronous. Then the value of the loading argument
    of the RentalItemsState's instance is set from false to true. The API
    method is then called to get the rental items response. The rental items
    state is updated with the items property of the response. If any exception
    occurs, the error state is set. Finally the loading state is set back to
    false.
    */
    private fun getRentalItems() {
        viewModelScope.launch {
            try {
                _rentalItemsState.value = _rentalItemsState.value.copy(loading = true)
                val rentalItemsRes = rentalItemsService.getRentalItemsByCategoryId(categoryId)
                _rentalItemsState.value =
                    _rentalItemsState.value.copy(list = rentalItemsRes.items)
                _rentalItemsState.value = _rentalItemsState.value.copy(categoryId = categoryId)
            } catch (e: Exception) {
                _rentalItemsState.value = _rentalItemsState.value.copy(error = e.toString())
            } finally {
                _rentalItemsState.value = _rentalItemsState.value.copy(loading = false)
            }
        }
    }


    /* When deleting a rental item, the value of the loading attribute
    of RentalItemDeleteState instance is changed to true. In that case,
    in the ConfirmRentalItemDelete composable's AlertDialog confirmation
    window, the Delete text button is disabled and the
    CircularProgressIndicator is displayed.

    The rental item is then removed from the backend based on the id in
    _rentalItemDeleteState. The removing is implemented using the
    removeRentalItem (API) interface method. With filter lambda, the value of
    _rentalItemsState's list argument is filtered by omitting an item that has
    the same rentalItemId than the rentalItemDeleteState's id. Then this new
    filtered list is set as the list state.

    The id argument of the _rentalItemDeleteState is then set to 0, which closes
    the confirmation window in RentalItemsScreen composable. Whether any error
    occurs or not, the loading state is reset to false indicating the end
    of the removal operation.
    */
    fun deleteRentalItem() {
        viewModelScope.launch {
            try {
                _rentalItemDeleteState.value = _rentalItemDeleteState.value.copy(loading = true)
                rentalItemsService.removeRentalItem(_rentalItemDeleteState.value.id)
                val categories = _rentalItemsState.value.list.filter {
                    it.rentalItemId != _rentalItemDeleteState.value.id
                }
                _rentalItemsState.value = _rentalItemsState.value.copy(list = categories)
                _rentalItemDeleteState.value = _rentalItemDeleteState.value.copy(id = 0)
            } catch (e: Exception) {
                _rentalItemDeleteState.value = _rentalItemDeleteState.value.copy(error = e.toString())
            } finally {
                _rentalItemDeleteState.value = _rentalItemDeleteState.value.copy(loading = false)
            }
        }
    }


    /* Public setter method for updating the properties of the rental item
    category state. category id is obtained from savedStateHandle.
    */
    fun setRentalItemAndCategory(rentalItemId: Int) {
        _rentalItemCategoryState.value = _rentalItemCategoryState.value.copy(
            categoryId = categoryId,
            rentalItemId = rentalItemId
        )
    }


    /* Sets the id of the rental item to be deleted in the
    _rentalItemDeleteState instance. This method is used in RentalItemsScreen
    when the trash can icon is clicked.
    */
    fun setDeletableRentalItemId(id: Int) {
        _rentalItemDeleteState.value = _rentalItemDeleteState.value.copy(id = id)
    }


    // Resets the error state:
    fun clearDeleteError() {
        _rentalItemDeleteState.value = _rentalItemDeleteState.value.copy(error = null)
    }

}
