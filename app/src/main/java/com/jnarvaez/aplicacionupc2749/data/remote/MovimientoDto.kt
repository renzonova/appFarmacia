package com.jnarvaez.aplicacionupc2749.data.remote

import com.google.gson.annotations.SerializedName

data class MovimientoDetalleDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("fecha") val fecha: String? = null,
    @SerializedName("tipo") val tipo: String? = null,
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("lote") val lote: String? = null,
    @SerializedName("cantidad") val cantidad: Int? = null,
    @SerializedName("responsable") val responsable: String? = null
)

data class MovimientoRequestDto(
    val tipo: String,
    val descripcion: String,
    val lote: String,
    val cantidad: Int,
    val responsable: String
)

data class MovimientoResponseDto(val id: String, val mensaje: String)
