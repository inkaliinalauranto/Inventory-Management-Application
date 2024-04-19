package com.example.inventorymanagementapplication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapplication.AccountDatabase
import com.example.inventorymanagementapplication.DbProvider
import com.example.inventorymanagementapplication.api.categoriesService
import com.example.inventorymanagementapplication.api.rentalItemsService
import com.example.inventorymanagementapplication.model.AddRentalItemReq
import com.example.inventorymanagementapplication.model.RentalItemState
import kotlinx.coroutines.launch

// JOS TÄSSÄ VÄLITETÄÄN TOINEN PARAMETRI
// (esim. private val db: AccountDatabase = DbProvider.db),
// OHJELMA KAATUU
class RentalItemAddViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _rentalItemState = mutableStateOf(RentalItemState())

    val rentalItemState: State<RentalItemState> = _rentalItemState

    private val categoryId = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0

    init {
        viewModelScope.launch {
            val accessToken = DbProvider.db.accountDao().getToken()
            println(accessToken)
        }
    }

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
                rentalItemsService.addRentalItem(
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