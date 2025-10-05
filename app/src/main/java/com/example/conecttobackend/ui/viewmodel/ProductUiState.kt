package com.example.conecttobackend.ui.viewmodel

import com.example.conecttobackend.data.model.Product

data class ProductUiState(
    val loading: Boolean = true,
    val items: List<Product> = emptyList(),
    val message: String? = null
)