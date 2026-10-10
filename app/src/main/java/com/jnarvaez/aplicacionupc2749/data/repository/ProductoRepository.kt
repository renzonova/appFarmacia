package com.jnarvaez.aplicacionupc2749.data.repository

import com.jnarvaez.aplicacionupc2749.data.remote.FarmaciaApiService
import com.jnarvaez.aplicacionupc2749.data.remote.LoteDto
import com.jnarvaez.aplicacionupc2749.data.remote.ProductoDto
import com.jnarvaez.aplicacionupc2749.data.remote.TipoDto
import com.jnarvaez.aplicacionupc2749.data.remote.UbicacionDto
import com.jnarvaez.aplicacionupc2749.data.remote.UnidadDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductoRepository @Inject constructor(
    private val apiService: FarmaciaApiService
) {

    suspend fun getProductos(): Result<List<ProductoDto>> {
        return try {
            val response = apiService.getProductos()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val err = response.errorBody()?.string() ?: "HTTP ${response.code()}"
                Result.failure(Exception(err))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun guardarProducto(producto: ProductoDto): Result<Boolean> {
        return try {
            val response = apiService.crearProducto(producto)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                val err = response.errorBody()?.string() ?: "HTTP ${response.code()}"
                Result.failure(Exception(err))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarProducto(codigo: String, producto: ProductoDto): Result<Boolean> {
        return try {
            val response = apiService.actualizarProducto(codigo, producto)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                val err = response.errorBody()?.string() ?: "HTTP ${response.code()}"
                Result.failure(Exception(err))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarProducto(codigo: String): Result<Boolean> {
        return try {
            val response = apiService.eliminarProducto(codigo)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                val err = response.errorBody()?.string() ?: "HTTP ${response.code()}"
                Result.failure(Exception(err))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTipos(): Result<List<TipoDto>> {
        return try {
            val response = apiService.getTipos()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLotes(): Result<List<LoteDto>> {
        return try {
            val response = apiService.getLotes()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUbicaciones(): Result<List<UbicacionDto>> {
        return try {
            val response = apiService.getUbicaciones()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUnidades(): Result<List<UnidadDto>> {
        return try {
            val response = apiService.getUnidades()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
