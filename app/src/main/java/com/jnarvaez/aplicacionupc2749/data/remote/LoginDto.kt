package com.jnarvaez.aplicacionupc2749.data.remote

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("usuario") val usuario: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("ok") val ok: Boolean = false,
    @SerializedName("usuario") val usuario: String? = null,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("rol") val rol: String? = null,
    @SerializedName("mensaje") val mensaje: String? = null
)