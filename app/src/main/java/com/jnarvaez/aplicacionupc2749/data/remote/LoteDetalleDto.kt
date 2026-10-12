package com.jnarvaez.aplicacionupc2749.data.remote

import com.google.gson.annotations.SerializedName

// Datos opcionales del detalle. No reemplaza LoteDto del catálogo.
data class LoteDetalleDto(
    @SerializedName("lote") val lote: String? = null,
    @SerializedName("vencimiento") val vencimiento: String? = null,
    @SerializedName("ubicacion") val ubicacion: String? = null,
    @SerializedName("stock") val stock: Int? = null,
    @SerializedName("estado") val estado: String? = null
)
