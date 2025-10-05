package com.example.conecttobackend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.example.conecttobackend.data.network.RetrofitInstance
import com.example.conecttobackend.data.repository.ProductRepository
import com.example.conecttobackend.ui.screens.ProductsScreen
import com.example.conecttobackend.ui.theme.ConectToBackendTheme
import com.example.conecttobackend.ui.viewmodel.ProductViewModelFactory
import com.example.conecttobackend.ui.viewmodel.ProductViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = RetrofitInstance.api
        val repository = ProductRepository(apiService)
        val factory = ProductViewModelFactory(repository)

        val viewModel: ProductViewModel by viewModels { factory }

        setContent {
            ConectToBackendTheme {
                ProductsScreen(vm = viewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ConectToBackendTheme {
        Greeting("Android")
    }
}