package com.jnarvaez.aplicacionupc2749

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jnarvaez.aplicacionupc2749.presentation.screens.inicio.DashboardInicioScreen
import com.jnarvaez.aplicacionupc2749.presentation.screens.inventario.InventarioScreen
import com.jnarvaez.aplicacionupc2749.presentation.screens.login.LoginScreen
import com.jnarvaez.aplicacionupc2749.ui.theme.AplicacionUPC2749Theme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AplicacionUPC2749Theme(dynamicColor = false) {
                var sesionIniciada by rememberSaveable { mutableStateOf(false) }
                var pantallaActual by rememberSaveable { mutableStateOf("inicio") }
                var codigoDetalleDirecto by rememberSaveable { mutableStateOf<String?>(null) }

                if (!sesionIniciada) {
                    LoginScreen(onLoginExitoso = { sesionIniciada = true })
                } else {
                    when (pantallaActual) {
                        "inicio" -> DashboardInicioScreen(
                            onInventarioClick = {
                                codigoDetalleDirecto = null
                                pantallaActual = "inventario"
                            },
                            onEscanearClick = {
                                codigoDetalleDirecto = null
                                pantallaActual = "escanear"
                            },
                            onMovimientosClick = {
                                codigoDetalleDirecto = null
                                pantallaActual = "movimientos"
                            },
                            onMasClick = {
                                codigoDetalleDirecto = null
                                pantallaActual = "mas"
                            },
                            onVerDetalleProducto = { codigo ->
                                codigoDetalleDirecto = codigo
                                pantallaActual = "inventario"
                            }
                        )

                        else -> InventarioScreen(
                            pantallaInicial = pantallaActual,
                            codigoInicialDetalle = codigoDetalleDirecto,
                            onInicioClick = {
                                codigoDetalleDirecto = null
                                pantallaActual = "inicio"
                            },
                            onCerrarSesion = {
                                sesionIniciada = false
                                pantallaActual = "inicio"
                                codigoDetalleDirecto = null
                            }
                        )
                    }
                }
            }
        }
    }
}