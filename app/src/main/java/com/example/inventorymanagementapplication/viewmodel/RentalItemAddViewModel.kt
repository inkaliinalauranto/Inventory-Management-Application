package com.example.inventorymanagementapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapplication.api.categoriesService
import com.example.inventorymanagementapplication.model.AddRentalItemReq
import com.example.inventorymanagementapplication.model.RentalItemState
import com.example.inventorymanagementapplication.model.RentalItemsState
import kotlinx.coroutines.launch

class RentalItemAddViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _rentalItemState = mutableStateOf(RentalItemState())

    val rentalItemState: State<RentalItemState> = _rentalItemState

    private val categoryId = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0

    fun setName(newName: String) {
        _rentalItemState.value = _rentalItemState.value.copy(rentalItemName = newName)
    }

    fun setDone(done: Boolean) {
        _rentalItemState.value = _rentalItemState.value.copy(done = done)
    }

    fun addRentalItem() {
        viewModelScope.launch {
            try {
                _rentalItemState.value = _rentalItemState.value.copy(loading = true)
                _rentalItemState.value = _rentalItemState.value.copy(categoryId = categoryId)
                println("KATEGORIA ADD! $categoryId")
                categoriesService.addRentalItem(
                    categoryId = categoryId,
                    AddRentalItemReq(
                        rentalItemName = _rentalItemState.value.rentalItemName,
                        createdByUserId = 1
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