package com.jnarvaez.aplicacionupc2749.presentation.screens.detalleproducto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jnarvaez.aplicacionupc2749.data.repository.DetalleProductoRepository
import com.jnarvaez.aplicacionupc2749.data.repository.MovimientoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalleProductoViewModel @Inject constructor(
    private val repository: DetalleProductoRepository,
    private val movimientos: MovimientoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DetalleProductoUiState())
    val uiState = _uiState.asStateFlow()

    private var contadorRegistro = 0

    fun nuevaSesion(): Int {
        contadorRegistro += 1
        return contadorRegistro
    }

    fun cargarProducto(codigo: String) {
        viewModelScope.launch {
            _uiState.value = DetalleProductoUiState(cargando = true)
            repository.getProducto(codigo).onSuccess { producto ->
                movimientos.consultar(producto.id).onSuccess { lista ->
                    _uiState.value = DetalleProductoUiState(
                        producto = producto, movimientos = lista)
                }.onFailure { error ->
                    _uiState.value = DetalleProductoUiState(producto = producto,
                        errorMovimientos = error.message ?: "No se pudo consultar movimientos")
                }
            }.onFailure { error ->
                _uiState.value = DetalleProductoUiState(mensajeError = error.message)
            }
        }
    }
}
