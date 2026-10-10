package com.jnarvaez.aplicacionupc2749

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jnarvaez.aplicacionupc2749.presentation.screens.inventario.InventarioScreen
import com.jnarvaez.aplicacionupc2749.ui.theme.AplicacionUPC2749Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AplicacionUPC2749Theme {
                InventarioScreen()
            }
        }
    }
}