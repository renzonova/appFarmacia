package com.jnarvaez.aplicacionupc2749.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FiltroChip(
    texto: String,
    seleccionado: Boolean = false,
    onClick: () -> Unit = {}
) {
    Text(
        text = texto,
        fontSize = 13.sp,
        color = if (seleccionado) {
            Color.White
        } else {
            Color(0xFF263238)
        },
        modifier = Modifier
            .background(
                color = if (seleccionado) {
                    Color(0xFF07566A)
                } else {
                    Color(0xFFEAF0F7)
                },
                shape = RoundedCornerShape(18.dp)
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 14.dp,
                vertical = 8.dp
            )
    )
}