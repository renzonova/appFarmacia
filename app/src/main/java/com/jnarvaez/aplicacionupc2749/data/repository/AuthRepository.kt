package com.jnarvaez.aplicacionupc2749.data.repository

import com.jnarvaez.aplicacionupc2749.data.remote.FarmaciaApiService
import com.jnarvaez.aplicacionupc2749.data.remote.LoginRequest
import com.jnarvaez.aplicacionupc2749.data.remote.LoginResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: FarmaciaApiService
) {

    suspend fun login(usuario: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(usuario, password))
            val body = response.body()

            if (response.isSuccessful && body != null && body.ok) {
                Result.success(body)
            } else if (response.code() == 401 || response.code() == 403) {
                Result.failure(Exception("Usuario o contraseña incorrectos"))
            } else if (response.isSuccessful && body != null) {
                Result.failure(Exception(body.mensaje ?: "No se pudo iniciar sesión"))
            } else {
                val err = response.errorBody()?.string() ?: "HTTP ${response.code()}"
                Result.failure(Exception(err))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}