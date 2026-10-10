package com.jnarvaez.aplicacionupc2749.data.remote

import com.google.gson.annotations.SerializedName

data class ProductoDto(
    @SerializedName("id") val id: String,
    @SerializedName("codigo") val codigo: String,
    @SerializedName("producto") val producto: String,
    @SerializedName("tipo") val tipo: String,
    @SerializedName("lote") val lote: String,
    @SerializedName("vencimiento") val vencimiento: String,
    @SerializedName("ubicacion") val ubicacion: String,
    @SerializedName("stock") val stock: Int,
    @SerializedName("minimo") val minimo: Int,
    @SerializedName("unidad") val unidad: String,
    @SerializedName("estado") val estado: String? = "normal"
)
