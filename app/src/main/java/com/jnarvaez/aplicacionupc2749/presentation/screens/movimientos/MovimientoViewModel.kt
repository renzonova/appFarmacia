package com.jnarvaez.aplicacionupc2749.presentation.screens.movimientos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jnarvaez.aplicacionupc2749.data.remote.MovimientoRequestDto
import com.jnarvaez.aplicacionupc2749.data.repository.MovimientoRepository
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
class MovimientoViewModel @Inject constructor(private val repository: MovimientoRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(MovimientoUiState())
    val uiState = _uiState.asStateFlow()

    fun registrar(id: String, datos: MovimientoRequestDto) {
        if (_uiState.value.guardando || _uiState.value.guardado) return
        viewModelScope.launch {
            _uiState.value = MovimientoUiState(guardando = true)
            repository.registrar(id, datos).onSuccess {
                _uiState.value = MovimientoUiState(guardado = true)
            }.onFailure {
                _uiState.value = MovimientoUiState(error = it.message)
            }
        }
    }
}
