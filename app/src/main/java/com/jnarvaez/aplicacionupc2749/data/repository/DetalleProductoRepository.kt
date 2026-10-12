package com.jnarvaez.aplicacionupc2749.data.repository

import com.jnarvaez.aplicacionupc2749.data.remote.FarmaciaApiService
import com.jnarvaez.aplicacionupc2749.data.remote.ProductoDto
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

// Complementa ProductoRepository; no lo sustituye.
class DetalleProductoRepository @Inject constructor(
    private val apiService: FarmaciaApiService
) {
    suspend fun getProducto(codigo: String): Result<ProductoDto> {
        return try {
            // El mapper existente utiliza id = codigo al guardar.
            val response = apiService.getProductoById(codigo)
            val producto = response.body()
            if (response.isSuccessful && producto != null) {
                if (producto.codigo == codigo) {
                    Result.success(producto)
                } else {
                    Result.failure(Exception("La respuesta corresponde a otro producto"))
                }
            } else {
                val mensaje = if (response.code() == 404)
                    "No se encontró el producto seleccionado"
                else "No se pudo consultar el producto (HTTP ${response.code()})"
                Result.failure(Exception(mensaje))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


