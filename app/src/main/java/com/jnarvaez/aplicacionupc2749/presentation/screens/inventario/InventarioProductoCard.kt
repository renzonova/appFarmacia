package com.jnarvaez.aplicacionupc2749.presentation.screens.inventario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jnarvaez.aplicacionupc2749.presentation.common.EstadoStock

@Composable
fun InventarioProductoCard(
    tipo: String,
    codigo: String,
    producto: String,
    lote: String,
    vencimiento: String,
    ubicacion: String,
    minimo: Int,
    stock: Int,
    unidad: String,
    estado: String,
    estadoTipo: String = "normal",
    accion: String = "Kárdex",
    onAccionClick: () -> Unit = {}
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            // CABECERA
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xFFDFF3F8),
                                RoundedCornerShape(3.dp)
                            )
                            .padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            )
                    ) {

                        Text(
                            text = tipo,
                            fontSize = 11.sp,
                            color = Color(0xFF18566B)
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(5.dp)
                    )

                    Text(
                        text = codigo,
                        fontSize = 13.sp,
                        color = Color(0xFF263238)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {

                    Text(
                        text = stock.toString(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (estadoTipo == "bajo") {
                            Color(0xFFFF8A00)
                        } else {
                            Color(0xFF07566A)
                        }
                    )

                    Text(
                        text = unidad,
                        fontSize = 9.sp,
                        color = Color(0xFF37474F)
                    )
                }
            }

            Spacer(
                modifier = Modifier.size(6.dp)
            )

            // PRODUCTO
            Text(
                text = producto,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF101820)
            )

            Spacer(
                modifier = Modifier.size(10.dp)
            )

            // INFORMACIÓN DEL PRODUCTO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFEAF2FC),
                        RoundedCornerShape(6.dp)
                    )
                    .padding(10.dp)
            ) {

                Column {

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "LOTE",
                                fontSize = 9.sp,
                                color = Color(0xFF53636C)
                            )

                            Spacer(
                                modifier = Modifier.size(3.dp)
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    imageVector = Icons.Default.QrCode2,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp),
                                    tint = Color(0xFF455A64)
                                )

                                Spacer(
                                    modifier = Modifier.width(4.dp)
                                )

                                Text(
                                    text = lote,
                                    fontSize = 14.sp,
                                    color = Color(0xFF17232A)
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "VENCIMIENTO",
                                fontSize = 9.sp,
                                color = Color(0xFF53636C)
                            )

                            Spacer(
                                modifier = Modifier.size(3.dp)
                            )

                            Text(
                                text = vencimiento,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (estadoTipo == "alerta") {
                                    Color(0xFFFF8A00)
                                } else {
                                    Color(0xFF17232A)
                                }
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.size(9.dp)
                    )

                    Text(
                        text = "UBICACIÓN FÍSICA",
                        fontSize = 9.sp,
                        color = Color(0xFF53636C)
                    )

                    Spacer(
                        modifier = Modifier.size(3.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp),
                            tint = Color(0xFF455A64)
                        )

                        Spacer(
                            modifier = Modifier.width(5.dp)
                        )

                        Text(
                            text = ubicacion,
                            modifier = Modifier.weight(1f),
                            fontSize = 14.sp,
                            color = Color(0xFF17232A)
                        )

                        Text(
                            text = "Mínimo: $minimo",
                            fontSize = 10.sp,
                            color = Color(0xFF455A64)
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.size(10.dp)
            )

            // ESTADO Y ACCIÓN
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                EstadoStock(
                    texto = estado,
                    tipo = estadoTipo
                )

                TextButton(
                    onClick = onAccionClick
                ) {

                    Text(
                        text = accion,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF07566A)
                    )

                    Icon(
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Color(0xFF07566A)
                    )
                }
            }
        }
    }
}