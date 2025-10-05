package com.example.conecttobackend.data.network

import com.example.conecttobackend.data.model.Product
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/api/products/")
    suspend fun getProducts(): List<Product>

    @POST("/api/products/")
    suspend fun createProduct(@Body product: Product): Product

    @PUT("/api/products/{id}")
    suspend fun updateProduct(@Path("id") id: Long, @Body product: Product): Product

    @DELETE("/api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Long)

    @GET("/api/products//search")
    suspend fun searchProductsByName(@Query("name") name: String): List<Product>
}