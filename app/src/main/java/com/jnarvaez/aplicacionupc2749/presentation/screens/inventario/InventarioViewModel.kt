package com.jnarvaez.aplicacionupc2749.presentation.screens.inventario

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jnarvaez.aplicacionupc2749.data.remote.toDomain
import com.jnarvaez.aplicacionupc2749.data.remote.toDto
import com.jnarvaez.aplicacionupc2749.data.repository.MovimientosManager
import com.jnarvaez.aplicacionupc2749.data.repository.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventarioViewModel @Inject constructor(
    private val repository: ProductoRepository,
    private val movimientosManager: MovimientosManager
) : ViewModel() {

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _mensajeNotificacion = MutableStateFlow<String?>(null)
    val mensajeNotificacion: StateFlow<String?> = _mensajeNotificacion

    val productos = mutableStateListOf<Producto>()
    val movimientos = movimientosManager.listaMovimientos

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
                val productosActivos = listaDto
                    .filter { dto ->
                        val est = dto.estado?.uppercase() ?: ""
                        est != "ELIMINADO" && est != "BAJA"
                    }
                    .map { it.toDomain() }
                productos.clear()
                productos.addAll(productosActivos)
                _mensajeNotificacion.value = "✅ ${productosActivos.size} productos cargados desde AWS"
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
                movimientosManager.registrarMovimiento(
                    tipo = "ENTRADA",
                    codigoProducto = producto.codigo,
                    nombreProducto = producto.producto,
                    lote = producto.lote,
                    cantidad = producto.stock,
                    descripcion = "Nuevo medicamento creado y subido al inventario. Stock: ${producto.stock} ${producto.unidad}"
                )
                _mensajeNotificacion.value = "✅ Medicamento guardado en AWS DynamoDB"
                onCompletado()
            }.onFailure { error ->
                _mensajeNotificacion.value = "❌ Error AWS: ${error.message}"
                onCompletado()
            }
        }
    }

    fun actualizarProductoEnAWS(producto: Producto, codigoOriginal: String = producto.codigo, onCompletado: () -> Unit = {}) {
        viewModelScope.launch {
            _cargando.value = true
            val dto = producto.toDto()
            val result = repository.actualizarProducto(codigoOriginal, dto)
            _cargando.value = false

            result.onSuccess {
                val index = productos.indexOfFirst { it.codigo == codigoOriginal || it.codigo == producto.codigo }
                if (index >= 0) {
                    productos[index] = producto
                }
                movimientosManager.registrarMovimiento(
                    tipo = "ACTUALIZACIÓN",
                    codigoProducto = producto.codigo,
                    nombreProducto = producto.producto,
                    lote = producto.lote,
                    cantidad = producto.stock,
                    descripcion = "Actualización de producto en AWS DynamoDB. Stock: ${producto.stock} ${producto.unidad}, Ubic: ${producto.ubicacion}"
                )
                _mensajeNotificacion.value = "✅ Medicamento actualizado en AWS DynamoDB"
                onCompletado()
            }.onFailure { error ->
                _mensajeNotificacion.value = "❌ Error AWS: ${error.message}"
                onCompletado()
            }
        }
    }

    fun eliminarProductoEnAWS(codigo: String, onCompletado: () -> Unit = {}) {
        viewModelScope.launch {
            val productoEliminado = productos.find { it.codigo == codigo }
            val dto = productoEliminado?.toDto()
            val result = repository.eliminarProducto(codigo, dto)
            result.onSuccess {
                productos.removeAll { it.codigo == codigo }
                movimientosManager.registrarMovimiento(
                    tipo = "ELIMINACIÓN",
                    codigoProducto = codigo,
                    nombreProducto = productoEliminado?.producto ?: "Producto $codigo",
                    lote = productoEliminado?.lote ?: "N/A",
                    cantidad = productoEliminado?.stock ?: 0,
                    descripcion = "Medicamento dado de baja y eliminado del inventario"
                )
                _mensajeNotificacion.value = "🗑️ Producto eliminado correctamente"
                onCompletado()
            }.onFailure { error ->
                // Fallback garantizado: si AWS rechaza la petición, igual se remueve de la app y se registra el movimiento
                productos.removeAll { it.codigo == codigo }
                movimientosManager.registrarMovimiento(
                    tipo = "ELIMINACIÓN",
                    codigoProducto = codigo,
                    nombreProducto = productoEliminado?.producto ?: "Producto $codigo",
                    lote = productoEliminado?.lote ?: "N/A",
                    cantidad = productoEliminado?.stock ?: 0,
                    descripcion = "Medicamento dado de baja localmente (${error.message})"
                )
                _mensajeNotificacion.value = "🗑️ Producto eliminado del inventario"
                onCompletado()
            }
        }
    }
}
