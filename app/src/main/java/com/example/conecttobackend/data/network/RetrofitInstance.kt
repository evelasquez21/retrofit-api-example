package com.example.conecttobackend.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://192.168.5.248:8080/"

    // Configuración  del interceptor de logging para ver las peticiones y respuestas en el Logcat
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Creación del cliente de OkHttp y le añade el interceptor
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Creación de la instancia de Retrofit usando lazy para que se inicialice solo cuando se necesite
    val api: ProductApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Usa el cliente con el logger
            .addConverterFactory(GsonConverterFactory.create()) // Usa Gson para convertir JSON
            .build()
            .create(ProductApi::class.java)
    }
}