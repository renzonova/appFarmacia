package com.jnarvaez.aplicacionupc2749.presentation.screens.inventario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditarProductoScreen(
    producto: Producto,
    onGuardar: (
        Producto
    ) -> Unit,
    onCancelar: () -> Unit
) {

    var codigo by remember {
        mutableStateOf(producto.codigo)
    }

    var nombre by remember {
        mutableStateOf(producto.producto)
    }

    var lote by remember {
        mutableStateOf(producto.lote)
    }

    var vencimiento by remember {
        mutableStateOf(producto.vencimiento)
    }

    var ubicacion by remember {
        mutableStateOf(producto.ubicacion)
    }

    var stock by remember {
        mutableStateOf(producto.stock.toString())
    }

    var minimo by remember {
        mutableStateOf(producto.minimo.toString())
    }

    var estado by remember {
        mutableStateOf(producto.estadoTipo)
    }

    var menuEstadoAbierto by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F8FC))
            .verticalScroll(
                rememberScrollState()
            )
            .padding(18.dp)
    ) {

        // ENCABEZADO

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "←",
                fontSize = 30.sp,
                color = Color(0xFF07566A),
                modifier = Modifier
                    .padding(end = 12.dp)
            )

            Column {

                Text(
                    text = "EDITAR PRODUCTO",
                    fontSize = 13.sp,
                    color = Color(0xFF19465A)
                )

                Text(
                    text = producto.codigo,
                    fontSize = 21.sp,
                    color = Color(0xFF101820)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // CÓDIGO

        Text(
            text = "Código",
            fontSize = 12.sp,
            color = Color(0xFF455A64)
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        OutlinedTextField(
            value = codigo,
            onValueChange = {
                codigo = it
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )


        // PRODUCTO

        Text(
            text = "Producto",
            fontSize = 12.sp,
            color = Color(0xFF455A64)
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )


        // LOTE

        Text(
            text = "Lote",
            fontSize = 12.sp,
            color = Color(0xFF455A64)
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        OutlinedTextField(
            value = lote,
            onValueChange = {
                lote = it
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )


        // VENCIMIENTO

        Text(
            text = "Vencimiento",
            fontSize = 12.sp,
            color = Color(0xFF455A64)
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        OutlinedTextField(
            value = vencimiento,
            onValueChange = {
                vencimiento = it
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = {
                Text(
                    text = "DD/MM/AAAA"
                )
            },
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )


        // UBICACIÓN

        Text(
            text = "Ubicación física",
            fontSize = 12.sp,
            color = Color(0xFF455A64)
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        OutlinedTextField(
            value = ubicacion,
            onValueChange = {
                ubicacion = it
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )


        // STOCK Y MÍNIMO

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Stock",
                    fontSize = 12.sp,
                    color = Color(0xFF455A64)
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                OutlinedTextField(
                    value = stock,
                    onValueChange = {
                        stock = it.filter { caracter ->
                            caracter.isDigit()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Stock mínimo",
                    fontSize = 12.sp,
                    color = Color(0xFF455A64)
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                OutlinedTextField(
                    value = minimo,
                    onValueChange = {
                        minimo = it.filter { caracter ->
                            caracter.isDigit()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )


        // ESTADO

        Text(
            text = "Estado del producto",
            fontSize = 12.sp,
            color = Color(0xFF455A64)
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Box {

            TextButton(
                onClick = {
                    menuEstadoAbierto = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White,
                        RoundedCornerShape(8.dp)
                    )
            ) {

                Text(
                    text = when (estado) {
                        "normal" -> "Normal"
                        "bajo" -> "Bajo Mínimo"
                        "alerta" -> "Próximo a Vencer"
                        else -> "Normal"
                    },
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF101820)
                )

                Text(
                    text = "▼",
                    color = Color(0xFF07566A)
                )
            }

            DropdownMenu(
                expanded = menuEstadoAbierto,
                onDismissRequest = {
                    menuEstadoAbierto = false
                }
            ) {

                DropdownMenuItem(
                    text = {
                        Text("Normal")
                    },
                    onClick = {
                        estado = "normal"
                        menuEstadoAbierto = false
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text("Bajo Mínimo")
                    },
                    onClick = {
                        estado = "bajo"
                        menuEstadoAbierto = false
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text("Próximo a Vencer")
                    },
                    onClick = {
                        estado = "alerta"
                        menuEstadoAbierto = false
                    }
                )
            }
        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )


        // BOTONES

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            TextButton(
                onClick = onCancelar,
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "CANCELAR",
                    color = Color(0xFF07566A)
                )
            }

            Button(
                onClick = {

                    val nuevoProducto = producto.copy(
                        codigo = codigo,
                        producto = nombre,
                        lote = lote,
                        vencimiento = vencimiento,
                        ubicacion = ubicacion,
                        stock = stock.toIntOrNull() ?: 0,
                        minimo = minimo.toIntOrNull() ?: 0,
                        estadoTipo = estado,
                        estado = when (estado) {
                            "normal" ->
                                "Stock Óptimo"

                            "bajo" ->
                                "Bajo Mínimo"

                            "alerta" ->
                                "Próximo a Vencer"

                            else ->
                                producto.estado
                        }
                    )

                    onGuardar(nuevoProducto)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF07566A)
                )
            ) {

                Text(
                    text = "GUARDAR",
                    color = Color.White
                )
            }
        }

        Spacer(
            modifier = Modifier.height(30.dp)
        )
    }
}