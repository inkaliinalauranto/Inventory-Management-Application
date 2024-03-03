package com.example.inventorymanagementapplication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventorymanagementapplication.viewmodel.CategoryEditViewModel

@Composable
fun CategoryEditScreen() {
    // An instance of the CategoryEditViewModel is created:
    val vm: CategoryEditViewModel = viewModel()

    Box(modifier = Modifier.fillMaxSize())
}