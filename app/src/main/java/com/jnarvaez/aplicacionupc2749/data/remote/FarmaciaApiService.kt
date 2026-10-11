package com.jnarvaez.aplicacionupc2749.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FarmaciaApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    @GET("productos")
    suspend fun getProductos(): Response<List<ProductoDto>>

    @GET("productos/{id}")
    suspend fun getProductoById(@Path("id") id: String): Response<ProductoDto>

    @POST("productos")
    suspend fun crearProducto(@Body producto: ProductoDto): Response<Map<String, Any>>

    @PUT("productos/{id}")
    suspend fun actualizarProducto(
        @Path("id") id: String,
        @Body producto: ProductoDto
    ): Response<Map<String, Any>>

    @DELETE("productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: String): Response<Map<String, Any>>

    @GET("tipos")
    suspend fun getTipos(): Response<List<TipoDto>>

    @GET("lotes")
    suspend fun getLotes(): Response<List<LoteDto>>

    @GET("ubicaciones")
    suspend fun getUbicaciones(): Response<List<UbicacionDto>>

    @GET("unidades")
    suspend fun getUnidades(): Response<List<UnidadDto>>
}
