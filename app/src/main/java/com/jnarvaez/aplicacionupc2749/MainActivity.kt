package com.jnarvaez.aplicacionupc2749

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jnarvaez.aplicacionupc2749.presentation.screens.inventario.InventarioScreen
import com.jnarvaez.aplicacionupc2749.ui.theme.AplicacionUPC2749Theme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.jnarvaez.aplicacionupc2749.presentation.screens.login.LoginScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AplicacionUPC2749Theme {
                var sesionIniciada by rememberSaveable { mutableStateOf(false) }

                if (sesionIniciada) {
                    InventarioScreen()
                } else {
                    LoginScreen(onLoginExitoso = { sesionIniciada = true })
                }
            }
        }
    }
}