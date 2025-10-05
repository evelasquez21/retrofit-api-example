package com.example.conecttobackend.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.conecttobackend.data.model.Product
import com.example.conecttobackend.data.model.ProductRequest
import com.example.conecttobackend.ui.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@Composable
fun ProductsScreen(vm: ProductViewModel) {
    // 1. Recolecta el estado del ViewModel de forma segura con el ciclo de vida
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    var scope = rememberCoroutineScope()

    var priceError by rememberSaveable { mutableStateOf<String?>(null) }

    // 2. Estado local para los campos del formulario y la búsqueda
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
                vm.messageShown()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
    ) {
        paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Barra de búsqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar por nombre...") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = {
                        if (searchQuery.isNotBlank()) vm.searchByName(searchQuery) else vm.loadAll()
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")

                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Formulario para crear un producto
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = price, onValueChange = {
                        price = it
                        priceError = null}, label = { Text("Precio") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth(), isError = priceError !=null, supportingText = { priceError?.let { Text(it) } })
                    OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Categoría") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            val priceDouble = price.toDoubleOrNull()
                            if (name.isBlank()){
                                return@Button
                            }
                            if (priceDouble != null && priceDouble > 0) {
                                vm.create(ProductRequest(name, priceDouble, category))
                                // Limpiar campos después de crear
                                name = ""
                                price = ""
                                category = ""
                            } else {
                                priceError = "Introduzca un precio valido o/y mayor a cero"
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Crear Producto")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            // Muestra indicador de carga o la lista de productos
            if (uiState.loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.items) { product ->
                        ProductItem(
                            product = product,
                            onUpdatePrice = {
                                val request = ProductRequest(
                                    name = product.name,
                                    price = product.price + 1.0,
                                    category = product.category
                                )
                                product.id?.let { id -> vm.update(id, request) }
                            },
                            onDelete = {
                                product.id?.let { id -> vm.delete(id) }
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ProductItem(
    product: Product,
    onUpdatePrice: () -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "Precio: $${"%.2f".format(product.price)}", style = MaterialTheme.typography.bodySmall)
            }
            Row {
                IconButton(onClick = onUpdatePrice) {
                    Icon(Icons.Default.AddCircle, contentDescription = "Subir Precio", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}