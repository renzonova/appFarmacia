package com.jnarvaez.aplicacionupc2749.presentation.screens.inventario

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jnarvaez.aplicacionupc2749.data.remote.toDomain
import com.jnarvaez.aplicacionupc2749.data.remote.toDto
import com.jnarvaez.aplicacionupc2749.data.repository.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventarioViewModel @Inject constructor(
    private val repository: ProductoRepository
) : ViewModel() {

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _mensajeNotificacion = MutableStateFlow<String?>(null)
    val mensajeNotificacion: StateFlow<String?> = _mensajeNotificacion

    val productos = mutableStateListOf<Producto>()

    init {
        cargarProductosDesdeAWS()
    }

    fun resetNotificacion() {
        _mensajeNotificacion.value = null
    }

    fun cargarProductosDesdeAWS() {
        viewModelScope.launch {
            _cargando.value = true
            val result = repository.getProductos()
            _cargando.value = false

            result.onSuccess { listaDto ->
                productos.clear()
                productos.addAll(listaDto.map { it.toDomain() })
                _mensajeNotificacion.value = "✅ ${listaDto.size} productos cargados desde AWS"
            }.onFailure { error ->
                _mensajeNotificacion.value = "⚠️ Error al cargar de AWS: ${error.message}"
            }
        }
    }

    fun guardarProductoEnAWS(producto: Producto, onCompletado: () -> Unit = {}) {
        viewModelScope.launch {
            _cargando.value = true
            val dto = producto.toDto()
            val result = repository.guardarProducto(dto)
            _cargando.value = false

            result.onSuccess {
                val index = productos.indexOfFirst { it.codigo == producto.codigo }
                if (index >= 0) {
                    productos[index] = producto
                } else {
                    productos.add(0, producto)
                }
                _mensajeNotificacion.value = "✅ Medicamento guardado en AWS DynamoDB"
                onCompletado()
            }.onFailure { error ->
                _mensajeNotificacion.value = "❌ Error AWS: ${error.message}"
                onCompletado()
            }
        }
    }

    fun actualizarProductoEnAWS(producto: Producto, onCompletado: () -> Unit = {}) {
        viewModelScope.launch {
            _cargando.value = true
            val dto = producto.toDto()
            val result = repository.actualizarProducto(dto.codigo, dto)
            _cargando.value = false

            result.onSuccess {
                val index = productos.indexOfFirst { it.codigo == producto.codigo }
                if (index >= 0) {
                    productos[index] = producto
                }
                _mensajeNotificacion.value = "✅ Medicamento actualizado en AWS DynamoDB"
                onCompletado()
            }.onFailure { error ->
                _mensajeNotificacion.value = "❌ Error AWS: ${error.message}"
                onCompletado()
            }
        }
    }

    fun eliminarProductoEnAWS(codigo: String) {
        viewModelScope.launch {
            val result = repository.eliminarProducto(codigo)
            result.onSuccess {
                productos.removeAll { it.codigo == codigo }
                _mensajeNotificacion.value = "🗑️ Producto eliminado de AWS DynamoDB"
            }.onFailure { error ->
                _mensajeNotificacion.value = "❌ Error AWS al eliminar: ${error.message}"
            }
        }
    }
}
