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
    @SerializedName("estado") val estado: String? = "normal",

    //campos opcionales:
    @SerializedName("gtin") val gtin: String? = null,
    @SerializedName("grupo") val grupo: String? = null,
    @SerializedName("presentacion") val presentacion: String? = null,
    @SerializedName("conservacion") val conservacion: String? = null,
    @SerializedName("humedad") val humedad: String? = null,
    @SerializedName("almacen") val almacen: String? = null,
    @SerializedName("lotesDetalle") val lotesDetalle: List<LoteDetalleDto>? = null

)
