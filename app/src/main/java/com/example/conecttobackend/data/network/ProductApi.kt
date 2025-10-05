package com.example.conecttobackend.data.network

import com.example.conecttobackend.data.model.Product
import com.example.conecttobackend.data.model.ProductRequest
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ProductApi {

    @GET("api/products")
    suspend fun getAllProducts(): List<Product>

    @GET("api/products/{id}")
    suspend fun getProductById(@Path("id") id: Long): Product

    @POST("api/products")
    suspend fun createProduct(@Body productRequest: ProductRequest): Product

    @PUT("api/products/{id}")
    suspend fun updateProduct(@Path("id") id: Long, @Body productRequest: ProductRequest): Product

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Long): Response<Unit>

    @GET("api/products/search")
    suspend fun searchProductsByName(@Query("productName") name: String): List<Product>
}