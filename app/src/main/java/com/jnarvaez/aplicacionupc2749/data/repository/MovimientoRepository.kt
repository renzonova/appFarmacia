package com.jnarvaez.aplicacionupc2749.data.repository

import com.jnarvaez.aplicacionupc2749.data.remote.*
import javax.inject.Inject

class MovimientoRepository @Inject constructor(private val api: FarmaciaApiService) {
    suspend fun consultar(id: String): Result<List<MovimientoDetalleDto>> {
        return try {
            val response = api.getMovimientos(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("No se pudo consultar movimientos: HTTP ${response.code()}"))
            }
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun registrar(id: String, datos: MovimientoRequestDto): Result<MovimientoResponseDto> {
        return try {
            val response = api.registrarMovimiento(id, datos)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("No se pudo confirmar el registro: HTTP ${response.code()}. Consulte antes de repetir."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("No se pudo confirmar el registro. Consulte movimientos antes de repetir."))
        }
    }
}


