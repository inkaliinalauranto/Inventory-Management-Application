package com.example.inventorymanagementapplication.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/* This view model is used for controlling data related modifying categories.
The value of the class attribute _id is set to the category id obtained from
savedStateHandle transmitted as a parameter. If there's no value or it
cannot be converted to integer, _id is set to 0.
*/
class CategoryEditViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    val _id = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0

    init {
        Log.d("TAG", "$_id")
    }
}