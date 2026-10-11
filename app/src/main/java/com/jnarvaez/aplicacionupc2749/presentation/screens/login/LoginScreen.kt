package com.jnarvaez.aplicacionupc2749.presentation.screens.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jnarvaez.aplicacionupc2749.data.remote.LoginResponse

@Composable
fun LoginScreen(
    onLoginExitoso: (LoginResponse) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val cargando by viewModel.cargando.collectAsState()
    val mensajeNotificacion by viewModel.mensajeNotificacion.collectAsState()
    val usuarioLogueado by viewModel.usuarioLogueado.collectAsState()

    // =====================================================
    // CAMPOS DEL FORMULARIO
    // =====================================================

    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verPassword by remember { mutableStateOf(false) }

    // =====================================================
    // NOTIFICACIONES (mismo patrón que InventarioScreen)
    // =====================================================

    LaunchedEffect(mensajeNotificacion) {
        mensajeNotificacion?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.resetNotificacion()
        }
    }

    // =====================================================
    // LOGIN CORRECTO -> AVISAR A MainActivity
    // =====================================================

    LaunchedEffect(usuarioLogueado) {
        usuarioLogueado?.let {
            onLoginExitoso(it)
            viewModel.resetLogin()
        }
    }

    val iniciarSesion = {
        focusManager.clearFocus()
        viewModel.iniciarSesion(usuario, password)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F8FC))
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Farmacia",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF07566A)
        )

        Text(
            text = "Inicia sesión para continuar",
            fontSize = 14.sp,
            color = Color(0xFF65727A)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // USUARIO

        Text(
            text = "Usuario",
            fontSize = 12.sp,
            color = Color(0xFF455A64),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !cargando,
            shape = RoundedCornerShape(8.dp),
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // CONTRASEÑA

        Text(
            text = "Contraseña",
            fontSize = 12.sp,
            color = Color(0xFF455A64),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !cargando,
            shape = RoundedCornerShape(8.dp),
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { verPassword = !verPassword }) {
                    Icon(
                        imageVector = if (verPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (verPassword) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            visualTransformation = if (verPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { iniciarSesion() })
        )

        Spacer(modifier = Modifier.height(24.dp))

        // BOTÓN INGRESAR

        Button(
            onClick = { iniciarSesion() },
            enabled = !cargando,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF07566A)
            )
        ) {
            if (cargando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            } else {
                Text(text = "INGRESAR")
            }
        }
    }
}