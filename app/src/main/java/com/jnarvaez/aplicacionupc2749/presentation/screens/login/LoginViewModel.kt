package com.jnarvaez.aplicacionupc2749.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jnarvaez.aplicacionupc2749.data.remote.LoginResponse
import com.jnarvaez.aplicacionupc2749.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _mensajeNotificacion = MutableStateFlow<String?>(null)
    val mensajeNotificacion: StateFlow<String?> = _mensajeNotificacion

    // Cuando el login es correcto se guarda aquí el usuario autenticado
    private val _usuarioLogueado = MutableStateFlow<LoginResponse?>(null)
    val usuarioLogueado: StateFlow<LoginResponse?> = _usuarioLogueado

    fun resetNotificacion() {
        _mensajeNotificacion.value = null
    }

    fun resetLogin() {
        _usuarioLogueado.value = null
    }

    fun iniciarSesion(usuario: String, password: String) {
        if (usuario.isBlank() || password.isBlank()) {
            _mensajeNotificacion.value = "⚠️ Ingresa usuario y contraseña"
            return
        }

        viewModelScope.launch {
            _cargando.value = true
            val result = repository.login(usuario.trim(), password)
            _cargando.value = false

            result.onSuccess { respuesta ->
                _mensajeNotificacion.value = "✅ Bienvenido ${respuesta.nombre ?: respuesta.usuario}"
                _usuarioLogueado.value = respuesta
            }.onFailure { error ->
                _mensajeNotificacion.value = "❌ ${error.message}"
            }
        }
    }
}