package com.jnarvaez.aplicacionupc2749.presentation.screens.movimientos

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jnarvaez.aplicacionupc2749.data.repository.MovimientoHistorial

private val AzulPrincipal = Color(0xFF07566A)
private val FondoPantalla = Color(0xFFF5F8FC)

@Composable
fun MovimientosScreen(
    movimientos: List<MovimientoHistorial>,
    onVolver: () -> Unit = {}
) {
    var busqueda by remember { mutableStateOf("") }
    var filtroTipo by remember { mutableStateOf("Todos") }

    val tiposFiltro = listOf("Todos", "Actualización", "Eliminación", "Entrada", "Salida", "Ajuste")

    val movimientosFiltrados = movimientos.filter { mov ->
        val coincideTipo = when (filtroTipo) {
            "Todos" -> true
            "Actualización" -> mov.tipo.contains("ACTUALIZA", ignoreCase = true)
            "Eliminación" -> mov.tipo.contains("ELIMINA", ignoreCase = true) || mov.tipo.contains("BAJA", ignoreCase = true)
            "Entrada" -> mov.tipo.contains("ENTRADA", ignoreCase = true) || mov.tipo.contains("INGRESO", ignoreCase = true)
            "Salida" -> mov.tipo.contains("SALIDA", ignoreCase = true)
            "Ajuste" -> mov.tipo.contains("AJUSTE", ignoreCase = true)
            else -> true
        }

        val coincideTexto = busqueda.isBlank() ||
                mov.nombreProducto.contains(busqueda, ignoreCase = true) ||
                mov.codigoProducto.contains(busqueda, ignoreCase = true) ||
                mov.lote.contains(busqueda, ignoreCase = true) ||
                mov.descripcion.contains(busqueda, ignoreCase = true)

        coincideTipo && coincideTexto
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoPantalla)
            .statusBarsPadding()
    ) {
        // ENCABEZADO
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = AzulPrincipal),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "MOVIMIENTOS DE INVENTARIO",
                            color = Color(0xFFA0D2DB),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "Historial y Trazabilidad",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    TextButton(onClick = onVolver) {
                        Text("‹ Volver", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Registro automático de actualizaciones, bajas, entradas y salidas de medicamentos.",
                    color = Color.White.copy(alpha = 0.82f),
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }

        // BUSCADOR Y FILTROS
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                placeholder = { Text("Buscar por medicamento, código o lote...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFF65727A)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = AzulPrincipal,
                    unfocusedIndicatorColor = Color(0xFFD6DFE6)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Chips de filtrado horizontal
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                tiposFiltro.forEach { tipo ->
                    val seleccionado = filtroTipo == tipo
                    FilterChip(
                        selected = seleccionado,
                        onClick = { filtroTipo = tipo },
                        label = {
                            Text(
                                text = tipo,
                                fontSize = 11.5.sp,
                                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AzulPrincipal,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Color(0xFF40484B)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = seleccionado,
                            borderColor = Color(0xFFD6DFE6),
                            selectedBorderColor = AzulPrincipal
                        )
                    )
                }
            }
        }

        // LISTA DE MOVIMIENTOS
        if (movimientosFiltrados.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "No hay movimientos registrados",
                        color = Color(0xFF64748B),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Los cambios y bajas de medicamentos aparecerán aquí.",
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(movimientosFiltrados, key = { it.id }) { item ->
                    TarjetaMovimiento(item)
                }
            }
        }
    }
}

@Composable
private fun TarjetaMovimiento(item: MovimientoHistorial) {
    val (colorBadge, fondoBadge, iconoBadge, textoSigno) = when {
        item.tipo.contains("ELIMINA", ignoreCase = true) || item.tipo.contains("BAJA", ignoreCase = true) ->
            Quad(Color(0xFFBA1A1A), Color(0xFFFFDAD6), Icons.Default.Delete, "BAJA")

        item.tipo.contains("ACTUALIZA", ignoreCase = true) ->
            Quad(Color(0xFFC2410C), Color(0xFFFFEDD5), Icons.Default.Edit, "STOCK")

        item.tipo.contains("ENTRADA", ignoreCase = true) || item.tipo.contains("INGRESO", ignoreCase = true) ->
            Quad(Color(0xFF1B6D24), Color(0xFFA0F399).copy(alpha = 0.5f), Icons.Default.ArrowDownward, "+${item.cantidad}")

        item.tipo.contains("SALIDA", ignoreCase = true) ->
            Quad(Color(0xFF0369A1), Color(0xFFE0F2FE), Icons.Default.ArrowUpward, "${item.cantidad}")

        else ->
            Quad(Color(0xFF7C3AED), Color(0xFFEDE9FE), Icons.Default.Tune, "AJUSTE")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Fila superior: Tipo, Badge e Indicador de Fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(fondoBadge, RoundedCornerShape(6.dp))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = iconoBadge,
                            contentDescription = null,
                            tint = colorBadge,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = item.tipo,
                            color = colorBadge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = item.fechaHora,
                    color = Color(0xFF64748B),
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Nombre del producto
            Text(
                text = item.nombreProducto,
                color = Color(0xFF0F172A),
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Código y Lote
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Cód: ${item.codigoProducto}",
                    color = Color(0xFF475569),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (item.lote.isNotBlank() && item.lote != "N/A") {
                    Text(
                        text = "•   Lote: ${item.lote}",
                        color = Color(0xFF475569),
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Descripción o motivo del movimiento
            Text(
                text = item.descripcion,
                color = Color(0xFF334155),
                fontSize = 11.5.sp,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Pie: Responsable
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "👤 Responsable: ${item.responsable}",
                    color = Color(0xFF64748B),
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = textoSigno,
                    color = colorBadge,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
