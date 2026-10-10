package com.jnarvaez.aplicacionupc2749.data.remote

import com.google.gson.annotations.SerializedName

data class TipoDto(
    @SerializedName("id") val id: String,
    @SerializedName("codigo") val codigo: String,
    @SerializedName("nombre") val nombre: String
)

data class LoteDto(
    @SerializedName("id") val id: String,
    @SerializedName("lote") val lote: String
)

data class UbicacionDto(
    @SerializedName("id") val id: String,
    @SerializedName("ubicacion") val ubicacion: String
)

data class UnidadDto(
    @SerializedName("id") val id: String,
    @SerializedName("unidad") val unidad: String
)
