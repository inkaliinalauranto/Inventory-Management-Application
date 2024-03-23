package com.example.inventorymanagementapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapplication.api.categoriesService
import com.example.inventorymanagementapplication.model.AddCategoryReq
import com.example.inventorymanagementapplication.model.CategoryState
import kotlinx.coroutines.launch

class CategoryAddViewModel : ViewModel() {
    /* The private attribute of the class representing the current category
    state relating to CategoryState data class which includes categoryName,
    loading, error and done attributes:
    */
    private val _categoryState = mutableStateOf(CategoryState())

    /* The public non-mutable variable representing the current category state
    providing read-only access to the category state:
    */
    val categoryState: State<CategoryState> = _categoryState


    /* A public setter method for updating the categoryName argument of the
    _categoryState's CategoryState instance:
    */
    fun setName(newName: String) {
        _categoryState.value = _categoryState.value.copy(categoryName = newName)
    }


    /* A public setter method for updating the done argument of the
    _categoryState's CategoryState instance:
    */
    fun setDone(done: Boolean) {
        _categoryState.value = _categoryState.value.copy(done = done)
    }


    /* A method for adding a category based on a name set by the user.
    A new category is added using the addCategory (API) interface method.
    The categoryName argument of the value in _categoryState is set to the
    categoryName argument of the data class object transferred as a
    parameter to the addCategory method.
    */
    fun addCategory() {
        viewModelScope.launch {
            try {
                _categoryState.value = _categoryState.value.copy(loading = true)
                categoriesService.addCategory(
                    AddCategoryReq(categoryName = _categoryState.value.categoryName)
                )
                setDone(true)
            } catch (e: Exception) {
                _categoryState.value = _categoryState.value.copy(error = e.toString())
            } finally {
                _categoryState.value = _categoryState.value.copy(loading = false)
            }
        }
    }
}