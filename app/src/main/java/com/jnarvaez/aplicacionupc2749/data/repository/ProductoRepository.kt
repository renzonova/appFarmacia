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
                val err = parseErrorBody(response.errorBody()?.string(), response.code())
                Result.failure(Exception(err))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarProducto(codigo: String, producto: ProductoDto): Result<Boolean> {
        return try {
            // El backend AWS DynamoDB gestiona la creación y actualización mediante
            // POST /productos (put_item upsert), ya que la función Lambda no implementa PUT /productos/{id}.
            val response = apiService.crearProducto(producto)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                // Fallback a PUT /productos/{id} por si se habilita en el backend en el futuro
                val responsePut = apiService.actualizarProducto(codigo, producto)
                if (responsePut.isSuccessful) {
                    Result.success(true)
                } else {
                    val err = parseErrorBody(response.errorBody()?.string(), response.code())
                    Result.failure(Exception(err))
                }
            }
        } catch (e: Exception) {
            try {
                val responsePut = apiService.actualizarProducto(codigo, producto)
                if (responsePut.isSuccessful) {
                    Result.success(true)
                } else {
                    Result.failure(e)
                }
            } catch (e2: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun eliminarProducto(codigo: String, productoDto: ProductoDto? = null): Result<Boolean> {
        return try {
            val response = apiService.eliminarProducto(codigo)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                val err = parseErrorBody(response.errorBody()?.string(), response.code())
                // Si AWS Lambda no tiene implementada la ruta DELETE,
                // realizamos un soft-delete en AWS DynamoDB vía POST /productos marcándolo como ELIMINADO
                if (err.contains("Ruta no soportada", ignoreCase = true) && productoDto != null) {
                    val eliminadoDto = productoDto.copy(estado = "ELIMINADO")
                    val responsePost = apiService.crearProducto(eliminadoDto)
                    if (responsePost.isSuccessful) {
                        Result.success(true)
                    } else {
                        Result.failure(Exception(err))
                    }
                } else {
                    Result.failure(Exception(err))
                }
            }
        } catch (e: Exception) {
            if (productoDto != null) {
                try {
                    val eliminadoDto = productoDto.copy(estado = "ELIMINADO")
                    val responsePost = apiService.crearProducto(eliminadoDto)
                    if (responsePost.isSuccessful) {
                        return Result.success(true)
                    }
                } catch (_: Exception) {}
            }
            Result.failure(e)
        }
    }

    private fun parseErrorBody(rawBody: String?, statusCode: Int): String {
        if (rawBody.isNullOrBlank()) return "HTTP $statusCode"
        return try {
            val json = org.json.JSONObject(rawBody)
            when {
                json.has("error") -> json.getString("error")
                json.has("message") -> json.getString("message")
                else -> rawBody
            }
        } catch (_: Exception) {
            rawBody
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
