package com.example.inventorymanagementapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapplication.api.categoriesService
import com.example.inventorymanagementapplication.model.CategoriesState
import com.example.inventorymanagementapplication.model.CategoryItem
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {
    /* The private attribute of the class representing the current categories
    state:
    */
    private val _categoriesState = mutableStateOf(CategoriesState())

    /* The public non-mutable variable representing the current login state
    providing read-only access to the categories state:
    */
    val categoriesState: State<CategoriesState> = _categoriesState


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
                _categoriesState.value = _categoriesState.value.copy(list = categoriesRes.categories)
            } catch (e: Exception) {
                _categoriesState.value = _categoriesState.value.copy(error = e.toString())
            } finally {
                _categoriesState.value = _categoriesState.value.copy(loading = false)
            }
        }
    }


    /* With filter lambda, the value of _categoriesState's list argument
    is filtered by omitting an item that has the same categoryId than
    the parameter's category_id. Then this new filtered list is set as
    list state.
    */
    fun deleteCategory(category: CategoryItem) {
        val categories = _categoriesState.value.list.filter {
            it.categoryId != category.categoryId
        }
        _categoriesState.value = _categoriesState.value.copy(list = categories)
    }
}