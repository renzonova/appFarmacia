package com.jnarvaez.aplicacionupc2749.presentation.screens.detalleproducto

import com.jnarvaez.aplicacionupc2749.data.remote.ProductoDto
import com.jnarvaez.aplicacionupc2749.data.remote.MovimientoDetalleDto

data class DetalleProductoUiState(
    val cargando: Boolean = false,
    val producto: ProductoDto? = null,
    val mensajeError: String? = null,
    val movimientos: List<MovimientoDetalleDto> = emptyList(),
    val errorMovimientos: String? = null
)
