package com.example.inventorymanagementapplication.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapplication.api.categoriesService
import com.example.inventorymanagementapplication.model.CategoriesState
import com.example.inventorymanagementapplication.model.CategoryDeleteState
import com.example.inventorymanagementapplication.model.CategoryItem
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {
    /* The private attribute of the class representing the current categories
    state:
    */
    private val _categoriesState = mutableStateOf(CategoriesState())

    /* The public non-mutable variable representing the current categories
    state providing read-only access to the categories state:
    */
    val categoriesState: State<CategoriesState> = _categoriesState

    /* The private attribute of the class representing the current state of
    an instance created from CategoryDeleteState data class, which is defined
    in Categories file:
    */
    private val _categoryDeleteState = mutableStateOf(CategoryDeleteState())

    /* The public non-mutable variable representing the state of a category
     to be deleted providing read-only access to its state:
    */
    val categoryDeleteState: State<CategoryDeleteState> = _categoryDeleteState


    /* When an instance is made from this class, the function inside
    init lambda is called:
    */
    init {
        getCategories()
    }


    /* A function for fetching categories from the API and updating the
    categories state accordingly. viewModelScope is used for coroutine scope
    since the API call is asynchronous. Then the value of the loading argument
    of the CategoriesState's instance is set from false to true. The API
    method is then called to get categories response. The categories state is
    updated with the categories argument of the response. If any exception
    occurs, the error state is set. Finally the loading state is set back to
    false.
    */
    private fun getCategories() {
        viewModelScope.launch {
            try {
                _categoriesState.value = _categoriesState.value.copy(loading = true)
                val categoriesRes = categoriesService.getCategories()
                _categoriesState.value =
                    _categoriesState.value.copy(list = categoriesRes.categories)
            } catch (e: Exception) {
                _categoriesState.value = _categoriesState.value.copy(error = e.toString())
            } finally {
                _categoriesState.value = _categoriesState.value.copy(loading = false)
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
    fun deleteCategory() {
        viewModelScope.launch {
            try {
                _categoryDeleteState.value = _categoryDeleteState.value.copy(loading = true)
                categoriesService.removeCategory(_categoryDeleteState.value.id)
                val categories = _categoriesState.value.list.filter {
                    it.categoryId != _categoryDeleteState.value.id
                }
                _categoriesState.value = _categoriesState.value.copy(list = categories)
                _categoryDeleteState.value = _categoryDeleteState.value.copy(id = 0)
            } catch (e: Exception) {
                _categoryDeleteState.value = _categoryDeleteState.value.copy(error = e.toString())
            } finally {
                _categoryDeleteState.value = _categoryDeleteState.value.copy(loading = false)
            }
        }
    }


    /* Sets the id of the category to be deleted in the _categoryDeleteState
    instance. This method is used in CategoriesScreen when the trash can icon
    is clicked.
    */
    fun setDeletableCategoryId(id: Int) {
        _categoryDeleteState.value = _categoryDeleteState.value.copy(id = id)
    }


    // Resets the error state:
    fun clearDeleteError() {
        _categoryDeleteState.value = _categoryDeleteState.value.copy(error = null)
    }

}