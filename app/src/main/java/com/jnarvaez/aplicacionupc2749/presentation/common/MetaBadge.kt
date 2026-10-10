package com.jnarvaez.aplicacionupc2749.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MetaBadge(text: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = Color(0xFFECEBFF)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3823A8),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}