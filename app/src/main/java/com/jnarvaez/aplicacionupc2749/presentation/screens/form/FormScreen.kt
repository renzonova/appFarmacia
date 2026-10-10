package com.jnarvaez.aplicacionupc2749.presentation.screens.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jnarvaez.aplicacionupc2749.presentation.screens.inventario.Producto
import java.time.LocalDate

data class TipoOpcion(val codigo: String, val descripcion: String)

@Composable
fun FormScreen(
    onGuardar: (Producto) -> Unit = {},
    onCancelar: () -> Unit = {}
) {
    // 🏷️ 3 TIPOS DE FÁRMACOS / PRODUCTOS
    val listaTipos = remember {
        listOf(
            TipoOpcion("PF", "PF - Fármaco / Medicamento"),
            TipoOpcion("DM", "DM - Dispositivo Médico"),
            TipoOpcion("PS", "PS - Producto Sanitario / Otro")
        )
    }

    // 📦 5 LOTES DISPONIBLES
    val listaLotes = remember {
        listOf("LT-8902A", "IN-4022", "MP-1055", "CV-4491", "GQ-8830")
    }

    // 📍 10 UBICACIONES FÍSICAS
    val listaUbicaciones = remember {
        listOf(
            "Anaquel A-01-N1",
            "Anaquel A-02-N1",
            "Anaquel A-04-N2",
            "Anaquel A-05-N3",
            "Gaveta G-01-C",
            "Gaveta G-03-A",
            "Gaveta G-08-B",
            "Refrigerador RF-01-B",
            "Estante E-01-Piso",
            "Estante E-05-Piso"
        )
    }

    // 🧪 3 TIPOS DE UNIDADES
    val listaUnidades = remember {
        listOf("CAJAS", "AMPOLLAS", "PARES")
    }

    // ESTADOS DEL FORMULARIO
    var codigo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var tipoSeleccionado by remember { mutableStateOf(listaTipos[0].codigo) }
    var tipoTexto by remember { mutableStateOf(listaTipos[0].descripcion) }
    var loteSeleccionado by remember { mutableStateOf(listaLotes[0]) }
    var vencimiento by remember { mutableStateOf("15/07/2027") } // Campo independiente
    var ubicacionSeleccionada by remember { mutableStateOf(listaUbicaciones[0]) }
    var unidadSeleccionada by remember { mutableStateOf(listaUnidades[0]) }
    var stock by remember { mutableStateOf("") }
    var minimo by remember { mutableStateOf("") }

    // ESTADOS MENÚS DESPLEGABLES
    var menuTipoAbierto by remember { mutableStateOf(false) }
    var menuLoteAbierto by remember { mutableStateOf(false) }
    var menuUbicacionAbierto by remember { mutableStateOf(false) }
    var menuUnidadAbierto by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F8FC))
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {
        // Encabezado
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "←",
                fontSize = 30.sp,
                color = Color(0xFF07566A),
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clickable { onCancelar() }
            )
            Text(
                text = "Nuevo Medicamento / Producto",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF07566A)
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        // 📌 CÓDIGO
        OutlinedTextField(
            value = codigo,
            onValueChange = { codigo = it },
            label = { Text("Código (ej. PF-00999)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 📌 NOMBRE DEL MEDICAMENTO
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del Producto / Medicamento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 📌 SELECCIÓN DE TIPO DE FÁRMACO (Categoría)
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = tipoTexto,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de Fármaco / Producto") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { menuTipoAbierto = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
            )
            DropdownMenu(
                expanded = menuTipoAbierto,
                onDismissRequest = { menuTipoAbierto = false }
            ) {
                listaTipos.forEach { itemTipo ->
                    DropdownMenuItem(
                        text = { Text(itemTipo.descripcion) },
                        onClick = {
                            tipoSeleccionado = itemTipo.codigo
                            tipoTexto = itemTipo.descripcion
                            menuTipoAbierto = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 📌 SELECCIÓN DE LOTE (5 Lotes independientes)
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = loteSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccionar Lote (5 disponibles)") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { menuLoteAbierto = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
            )
            DropdownMenu(
                expanded = menuLoteAbierto,
                onDismissRequest = { menuLoteAbierto = false }
            ) {
                listaLotes.forEach { itemLote ->
                    DropdownMenuItem(
                        text = { Text("Lote: $itemLote") },
                        onClick = {
                            loteSeleccionado = itemLote
                            menuLoteAbierto = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 📌 FECHA DE VENCIMIENTO (Independiente)
        OutlinedTextField(
            value = vencimiento,
            onValueChange = { vencimiento = it },
            label = { Text("Fecha de Vencimiento (DD/MM/YYYY)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 📌 SELECCIÓN DE UBICACIÓN FÍSICA (10 Ubicaciones)
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = ubicacionSeleccionada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccionar Ubicación Física (10 disponibles)") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { menuUbicacionAbierto = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
            )
            DropdownMenu(
                expanded = menuUbicacionAbierto,
                onDismissRequest = { menuUbicacionAbierto = false }
            ) {
                listaUbicaciones.forEach { itemUbi ->
                    DropdownMenuItem(
                        text = { Text(itemUbi) },
                        onClick = {
                            ubicacionSeleccionada = itemUbi
                            menuUbicacionAbierto = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 📌 SELECCIÓN DE UNIDAD (Cajas, Ampollas, Pares)
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = unidadSeleccionada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccionar Unidad de Medida") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { menuUnidadAbierto = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
            )
            DropdownMenu(
                expanded = menuUnidadAbierto,
                onDismissRequest = { menuUnidadAbierto = false }
            ) {
                listaUnidades.forEach { itemUni ->
                    DropdownMenuItem(
                        text = { Text(itemUni) },
                        onClick = {
                            unidadSeleccionada = itemUni
                            menuUnidadAbierto = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Stock Inicial
        OutlinedTextField(
            value = stock,
            onValueChange = { stock = it },
            label = { Text("Stock Inicial") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Stock Mínimo
        OutlinedTextField(
            value = minimo,
            onValueChange = { minimo = it },
            label = { Text("Stock Mínimo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TextButton(
                onClick = onCancelar,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "CANCELAR", color = Color(0xFF07566A))
            }

            Button(
                onClick = {
                    val finalCodigo = if (codigo.isNotBlank()) codigo else "${tipoSeleccionado}-${(1000..9999).random()}"
                    val finalNombre = if (nombre.isNotBlank()) nombre else "Nuevo Medicamento"
                    val finalVencimiento = if (vencimiento.isNotBlank()) vencimiento else "31/12/2026"

                    val parsedDate = try {
                        val parts = finalVencimiento.split("/")
                        if (parts.size == 3) {
                            LocalDate.of(parts[2].toInt(), parts[1].toInt(), parts[0].toInt())
                        } else {
                            LocalDate.now()
                        }
                    } catch (e: Exception) {
                        LocalDate.now()
                    }

                    val nuevoProducto = Producto(
                        tipo = tipoSeleccionado,
                        codigo = finalCodigo,
                        producto = finalNombre,
                        lote = loteSeleccionado,
                        vencimiento = finalVencimiento,
                        fechaVencimiento = parsedDate,
                        ubicacion = ubicacionSeleccionada,
                        minimo = minimo.toIntOrNull() ?: 10,
                        stock = stock.toIntOrNull() ?: 0,
                        unidad = unidadSeleccionada,
                        estado = "Stock Óptimo",
                        estadoTipo = "normal",
                        accion = "Kárdex"
                    )
                    onGuardar(nuevoProducto)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF07566A))
            ) {
                Text(text = "CREAR Y SUBIR", color = Color.White)
            }
        }
    }
}