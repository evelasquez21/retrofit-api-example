package com.example.conecttobackend.data.model

data class Product(
    val id: Long?,
    val name: String,
    val price: Double,
    val category: String?,
    val createdAt: String?
)

data class ProductRequest(
    val name: String,
    val price: Double,
    val category: String?
)