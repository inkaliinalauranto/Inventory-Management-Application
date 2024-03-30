package com.example.inventorymanagementapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapplication.api.categoriesService
import com.example.inventorymanagementapplication.model.ItemDeleteState
import com.example.inventorymanagementapplication.model.ItemsState
import kotlinx.coroutines.launch

class ItemsViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    val id = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0

    /* The private attribute of the class representing the current items
    state:
    */
    private val _itemsState = mutableStateOf(ItemsState())

    /* The public non-mutable variable representing the current items
    state providing read-only access:
    */
    val itemsState: State<ItemsState> = _itemsState

    /* The private attribute of the class representing the current state of
    an instance created from CategoryDeleteState data class, which is defined
    in Categories file:
    */
    private val _itemDeleteState = mutableStateOf(ItemDeleteState())

    /* The public non-mutable variable representing the state of a item
     to be deleted providing read-only access to its state:
    */
    val itemDeleteState: State<ItemDeleteState> = _itemDeleteState


    /* When an instance is made from this class, the function inside
    init lambda is called:
    */
    init {
        getItems()
    }


    /* A function for fetching items from the API and updating the
    items state accordingly. viewModelScope is used for coroutine scope
    since the API call is asynchronous. Then the value of the loading argument
    of the ItemsState's instance is set from false to true. The API
    method is then called to get the items response. The items state is
    updated with the items argument of the response. If any exception
    occurs, the error state is set. Finally the loading state is set back to
    false.
    */
    private fun getItems() {
        viewModelScope.launch {
            try {
                _itemsState.value = _itemsState.value.copy(loading = true)
                val itemsRes = categoriesService.getItemsByCategoryId(id)
                _itemsState.value =
                    _itemsState.value.copy(list = itemsRes.items)
            } catch (e: Exception) {
                _itemsState.value = _itemsState.value.copy(error = e.toString())
            } finally {
                _itemsState.value = _itemsState.value.copy(loading = false)
            }
        }
    }


    /* When deleting a category, the value of the loading
    attribute of the CategoryDeleteState instance is changed to
    true. In that case, in the ConfirmCategoryDelete composable's
    AlertDialog confirmation window, the Delete text button is
    disabled and the CircularProgressIndicator is displayed.

    The category is then removed from the backend based on the id in
    _categoryDeleteState. The removing is implemented using the removeCategory
    (API) interface method. With filter lambda, the value of
    _categoriesState's list argument is filtered by omitting an item that has
    the same categoryId than the categoryDeleteState's id. Then this new
    filtered list is set as the list state.

    The id argument of the _categoryDeleteState is then set to 0, which closes
    the confirmation window in CategoriesScreen composable. Whether any error
    occurs or not, the loading state is reset to false indicating the end
    of the removal operation.
    */
//    fun deleteCategory() {
//        viewModelScope.launch {
//            try {
//                _itemDeleteState.value = _itemDeleteState.value.copy(loading = true)
//                categoriesService.removeCategory(_itemDeleteState.value.id)
//                val categories = _itemsState.value.list.filter {
//                    it.categoryId != _itemDeleteState.value.id
//                }
//                _itemsState.value = _itemsState.value.copy(list = categories)
//                _itemDeleteState.value = _itemDeleteState.value.copy(id = 0)
//            } catch (e: Exception) {
//                _itemDeleteState.value = _itemDeleteState.value.copy(error = e.toString())
//            } finally {
//                _itemDeleteState.value = _itemDeleteState.value.copy(loading = false)
//            }
//        }
//    }
//
//
//    /* Sets the id of the category to be deleted in the _categoryDeleteState
//    instance. This method is used in CategoriesScreen when the trash can icon
//    is clicked.
//    */
//    fun setDeletableCategoryId(id: Int) {
//        _itemDeleteState.value = _itemDeleteState.value.copy(id = id)
//    }
//
//
//    // Resets the error state:
//    fun clearDeleteError() {
//        _itemDeleteState.value = _itemDeleteState.value.copy(error = null)
//    }

}
