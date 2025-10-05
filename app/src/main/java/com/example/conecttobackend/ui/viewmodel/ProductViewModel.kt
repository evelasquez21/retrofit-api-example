package com.example.conecttobackend.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.conecttobackend.data.model.ProductRequest
import com.example.conecttobackend.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        loadAll()
    }

    fun loadAll() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, message = null) }
            runCatching {
                repository.listAll()
            }.onSuccess { products ->
                _uiState.update { it.copy(loading = false, items = products) }
            }.onFailure { throwable ->
                _uiState.update { it.copy(loading = false, message = "Error: ${throwable.message}") }
            }
        }
    }

    fun searchByName(name: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, message = null) }
            runCatching {
                repository.search(name)
            }.onSuccess { products ->
                _uiState.update { it.copy(loading = false, items = products) }
            }.onFailure { throwable ->
                _uiState.update { it.copy(loading = false, message = "Error: ${throwable.message}") }
            }
        }
    }

    fun create(productRequest: ProductRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, message = null) }
            runCatching {
                repository.create(productRequest)
            }.onSuccess {
                _uiState.update { it.copy(message = "Producto creado exitosamente.") }
                loadAll() // Recarga la lista para mostrar el nuevo item
            }.onFailure { throwable ->
                _uiState.update { it.copy(loading = false, message = "Error al crear: ${throwable.message}") }
            }
        }
    }

    fun update(id: Long, productRequest: ProductRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, message = null) }
            runCatching {
                repository.update(id, productRequest)
            }.onSuccess {
                _uiState.update { it.copy(message = "Producto actualizado.") }
                loadAll() // Recarga la lista para reflejar los cambios
            }.onFailure { throwable ->
                _uiState.update { it.copy(loading = false, message = "Error al actualizar: ${throwable.message}") }
            }
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, message = null) }
            runCatching {
                repository.delete(id)
            }.onSuccess {
                _uiState.update { it.copy(message = "Producto eliminado.") }
                loadAll() // Recarga la lista para quitar el item borrado
            }.onFailure { throwable ->
                _uiState.update { it.copy(loading = false, message = "Error al eliminar: ${throwable.message}") }
            }
        }
    }

    fun messageShown() {
        _uiState.update { it.copy(message = null) }
    }
}

class ProductViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}