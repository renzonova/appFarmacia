package com.jnarvaez.aplicacionupc2749.presentation.screens.movimientos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jnarvaez.aplicacionupc2749.data.remote.MovimientoRequestDto
import com.jnarvaez.aplicacionupc2749.data.repository.MovimientoRepository
import com.jnarvaez.aplicacionupc2749.data.repository.MovimientosManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovimientoUiState(
    val guardando: Boolean = false,
    val guardado: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MovimientoViewModel @Inject constructor(
    private val repository: MovimientoRepository,
    private val movimientosManager: MovimientosManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(MovimientoUiState())
    val uiState = _uiState.asStateFlow()

    fun registrar(id: String, datos: MovimientoRequestDto, productoNombre: String = "") {
        if (_uiState.value.guardando || _uiState.value.guardado) return
        viewModelScope.launch {
            _uiState.value = MovimientoUiState(guardando = true)
            repository.registrar(id, datos).onSuccess {
                movimientosManager.registrarMovimiento(
                    tipo = datos.tipo,
                    codigoProducto = id,
                    nombreProducto = productoNombre.ifBlank { "Producto $id" },
                    lote = datos.lote,
                    cantidad = datos.cantidad,
                    responsable = datos.responsable,
                    descripcion = datos.descripcion
                )
                _uiState.value = MovimientoUiState(guardado = true)
            }.onFailure {
                // Registrar también para trazabilidad local
                movimientosManager.registrarMovimiento(
                    tipo = datos.tipo,
                    codigoProducto = id,
                    nombreProducto = productoNombre.ifBlank { "Producto $id" },
                    lote = datos.lote,
                    cantidad = datos.cantidad,
                    responsable = datos.responsable,
                    descripcion = datos.descripcion
                )
                _uiState.value = MovimientoUiState(guardado = true)
            }
        }
    }
}
