package com.example.inventorymanagementapplication.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapplication.api.categoriesService
import com.example.inventorymanagementapplication.model.CategoryState
import com.example.inventorymanagementapplication.model.UpdateCategoryReq
import kotlinx.coroutines.launch

// This view model is used for controlling data related modifying a category.
class CategoryEditViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    /* The private attribute of the class representing the current category
    state relating to CategoryState data class which includes categoryName,
    loading, done and error attributes:
    */
    private val _categoryState = mutableStateOf(CategoryState())

    /* The public non-mutable variable representing the current category state
    providing read-only access to the category state:
    */
    val categoryState: State<CategoryState> = _categoryState

    /* The value of the id variable is set to the category id obtained from
    savedStateHandle transmitted as a parameter. If there's no value or it
    cannot be converted to integer, id is set to 0.
    */
    val id = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0


    /* The method inside init lambda is called immediately when an instance
    of this class is created:
    */
    init {
        getCategoryById()
    }


    /* A function for fetching a category by id from the API and updating the
    category state accordingly. viewModelScope is used for coroutine scope
    since the API call is asynchronous. Then the value of the loading argument
    of the CategoryState's instance is set from false to true. The
    getCategoryById (API) interface method is then called to get the category
    response. The category state is updated with the categoryName argument of
    the response. If any exception occurs, the error state is set. Finally the
    loading state is set back to false.
    */
    private fun getCategoryById() {
        try {
            viewModelScope.launch {
                _categoryState.value = _categoryState.value.copy(loading = true)
                val response = categoriesService.getCategoryById(id)
                _categoryState.value =
                    _categoryState.value.copy(categoryName = response.category.categoryName)
            }
        } catch (e: Exception) {
            _categoryState.value = _categoryState.value.copy(error = e.toString())
        } finally {
            _categoryState.value = _categoryState.value.copy(loading = false)
        }
    }


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


    /* A method for fetching a category by id from the API and updating the
    category name using the editCategory (API) interface method. The id and
    categoryName arguments are given as parameters to the editCategory method.
    The value of the categoryName parameter is set to the categoryName value
    of the category state's instance. _categoryState's done argument is set
    to true which activates the LaunchedEffect lambda in CategoryEditScreen
    and fulfils the condition so that the navigation to the CategoriesScreen
    is implemented.
    */
    fun editCategory() {
        viewModelScope.launch {
            try {
                _categoryState.value = _categoryState.value.copy(loading = true)
                categoriesService.editCategory(
                    id,
                    UpdateCategoryReq(categoryName = _categoryState.value.categoryName)
                )
                setDone(done = true)
            } catch (e: Exception) {
                _categoryState.value = _categoryState.value.copy(error = e.toString())
            } finally {
                _categoryState.value = _categoryState.value.copy(loading = false)
            }
        }
    }
}