package com.example.conecttobackend.data.repository

import com.example.conecttobackend.data.model.Product
import com.example.conecttobackend.data.model.ProductRequest
import com.example.conecttobackend.data.network.ProductApi
import retrofit2.Response

class ProductRepository(private val apiService: ProductApi) {

    suspend fun listAll(): List<Product> {
        return apiService.getAllProducts()
    }

    suspend fun get(id: Long): Product {
        return apiService.getProductById(id)
    }

    suspend fun create(productRequest: ProductRequest): Product {
        return apiService.createProduct(productRequest)
    }

    suspend fun update(id: Long, productRequest: ProductRequest): Product {
        return apiService.updateProduct(id, productRequest)
    }

    suspend fun delete(id: Long): Response<Unit> {
        return apiService.deleteProduct(id)
    }

    suspend fun search(name: String): List<Product> {
        return apiService.searchProductsByName(name)
    }
}