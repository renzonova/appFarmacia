package com.jnarvaez.aplicacionupc2749.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EstadoStock(
    texto: String,
    tipo: String = "normal",
    seleccionado: Boolean = false,
    onClick: () -> Unit = {}
) {

    val fondo = when (tipo) {
        "bajo" -> Color(0xFFFFE3A3)
        "alerta" -> Color(0xFFFFDCC8)
        "regular" -> Color(0xFFE7F4E7)
        else -> Color(0xFFB9F2A8)
    }

    val textoColor = when (tipo) {
        "bajo" -> Color(0xFF6A4A00)
        "alerta" -> Color(0xFFB45B00)
        "regular" -> Color(0xFF267A35)
        else -> Color(0xFF176B2C)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = if (seleccionado) {
                    Color(0xFF07566A)
                } else {
                    fondo
                },
                shape = RoundedCornerShape(18.dp)
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 12.dp,
                vertical = 6.dp
            )
    ) {

        Text(
            text = "●",
            color = if (seleccionado) Color.White else textoColor,
            fontSize = 10.sp
        )

        Text(
            text = " $texto",
            color = if (seleccionado) Color.White else textoColor,
            fontSize = 12.sp
        )
    }
}