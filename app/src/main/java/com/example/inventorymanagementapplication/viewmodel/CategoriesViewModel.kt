package com.example.inventorymanagementapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.inventorymanagementapplication.model.CategoriesState

class CategoriesViewModel : ViewModel() {
    /* The private attribute of the class representing the current categories
    state:
    */
    private val _categoriesState = mutableStateOf(CategoriesState())

    /* The public non-mutable variable representing the current login state
    providing read-only access to the categories state:
    */
    val categoriesState: State<CategoriesState> = _categoriesState
}