package com.jnarvaez.aplicacionupc2749.presentation.screens.detalleproducto

import com.jnarvaez.aplicacionupc2749.presentation.screens.movimientos.RegistrarMovimientoScreen
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jnarvaez.aplicacionupc2749.data.remote.ProductoDto
import com.jnarvaez.aplicacionupc2749.data.remote.LoteDetalleDto
import com.jnarvaez.aplicacionupc2749.data.remote.MovimientoDetalleDto
import com.jnarvaez.aplicacionupc2749.presentation.common.MetaBadge
import com.jnarvaez.aplicacionupc2749.ui.theme.AplicacionUPC2749Theme

private val AzulDetalle = Color(0xFF07566A)
private val FondoDetalle = Color(0xFFF4F7FF)

@Composable
fun DetalleProductoScreen(
    codigo: String,
    onVolver: () -> Unit,
    viewModel: DetalleProductoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(codigo) { viewModel.cargarProducto(codigo) }
    var mostrarRegistro by rememberSaveable(codigo) { mutableStateOf(false) }
    var sesion by rememberSaveable { mutableStateOf(0) }
    val producto = uiState.producto
    if (mostrarRegistro && producto != null) {
        RegistrarMovimientoScreen(producto, sesion.toString(), onVolver = {
            mostrarRegistro = false
            viewModel.cargarProducto(codigo)
        })
        return
    }
    BackHandler(onBack = onVolver)
    DetalleProductoContenido(uiState, onVolver,
        onReintentar = { viewModel.cargarProducto(codigo) },
        onRegistrar = { sesion = viewModel.nuevaSesion(); mostrarRegistro = true })
}

@Composable
private fun DetalleProductoContenido(
    uiState: DetalleProductoUiState,
    onVolver: () -> Unit,
    onReintentar: () -> Unit,
    onRegistrar: () -> Unit
) {
    Column(Modifier.fillMaxSize().background(FondoDetalle)
        .windowInsetsPadding(WindowInsets.safeDrawing).padding(16.dp)) {
        Text("Detalle del producto", color = AzulDetalle,
            style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        TextButton(onClick = onVolver) { Text("‹ Regresar al inventario") }
        if (uiState.cargando) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AzulDetalle)
            }
        } else if (uiState.mensajeError != null || uiState.producto == null) {
            Text(uiState.mensajeError ?: "No hay un producto seleccionado")
            Button(onClick = onReintentar) { Text("Reintentar") }
        } else {
            FichaProducto(uiState.producto, onRegistrar, uiState.errorMovimientos, uiState.movimientos)
        }
    }
}

@Composable
private fun FichaProducto(producto: ProductoDto, onRegistrar: () -> Unit, errorMovimientos: String?, movimientos: List<MovimientoDetalleDto>) {
    var verTodos by remember { mutableStateOf(false) }
    // Una fila antigua conserva el lote y stock que ya tenía.
    val lotes = producto.lotesDetalle?.takeIf { it.isNotEmpty() }
        ?: listOf(LoteDetalleDto(producto.lote, producto.vencimiento,
            producto.ubicacion, producto.stock, producto.estado))
    Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = AzulDetalle)) {
            Column(Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Inventory2, contentDescription = null,
                        tint = Color.White, modifier = Modifier.size(40.dp))
                    Spacer(Modifier.width(12.dp))
                    Text("${producto.tipo} · Producto farmacéutico", color = Color.White)
                }
                Text(producto.producto, color = Color.White,
                    style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(dato(producto.presentacion), color = Color.White)
            }
        }
        BloqueDetalle {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DatoDetalle("Código institucional", producto.codigo, Modifier.weight(1f))
                DatoDetalle("GTIN / Código de barras", dato(producto.gtin), Modifier.weight(1f))
            }
            HorizontalDivider(color = FondoDetalle)
            DatoDetalle("Grupo", dato(producto.grupo))
            DatoDetalle("Presentación", dato(producto.presentacion))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Card(Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Stock registrado", color = AzulDetalle)
                    Text(producto.stock.toString(), style = MaterialTheme.typography.headlineMedium,
                        color = AzulDetalle, fontWeight = FontWeight.Bold)
                    Text(producto.unidad, style = MaterialTheme.typography.bodySmall)
                    MetaBadge("Mín.: ${producto.minimo}")
                    Text(dato(producto.estado), style = MaterialTheme.typography.bodySmall)
                }
            }
            Card(Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    DatoDetalle("Conservación", dato(producto.conservacion))
                    DatoDetalle("Humedad", dato(producto.humedad))
                }
            }
        }
        BloqueDetalle {
            DatoDetalle("Almacén", dato(producto.almacen))
            DatoDetalle("Ubicación", producto.ubicacion)
        }
        Text("Lotes registrados", color = AzulDetalle,
            style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("${lotes.size} lote(s)", style = MaterialTheme.typography.bodySmall)
        // Se mantiene el orden recibido. No se afirma prioridad FEFO ni liberación.
        lotes.forEach { lote ->
            BloqueDetalle {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    MetaBadge("Lote: ${dato(lote.lote)}")
                }
                DatoDetalle("Ubicación física", dato(lote.ubicacion))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DatoDetalle("Fecha de vencimiento", dato(lote.vencimiento), Modifier.weight(1f))
                    DatoDetalle("Stock del lote",
                        lote.stock?.let { "$it ${producto.unidad}" } ?: "No registrado",
                        Modifier.weight(1f))
                }
                DatoDetalle("Estado", dato(lote.estado))
            }
        }
        Text("Acciones operativas", color = AzulDetalle,
            style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Button(onClick = onRegistrar, modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = AzulDetalle)) {
            Text("Registrar movimiento")
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = {}, enabled = false, modifier = Modifier.weight(1f)) {
                Text("Contar lote")
            }
            OutlinedButton(onClick = {}, enabled = false, modifier = Modifier.weight(1f)) {
                Text("Cuarentena / Dev.")
            }
        }
        Text("Conteo y cuarentena fuera del registro de movimientos.",
            style = MaterialTheme.typography.bodySmall)
        Text("Trazabilidad reciente", color = AzulDetalle,
            style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        BloqueDetalle {
            if (movimientos.isEmpty()) {
                Text(errorMovimientos ?: "Sin movimientos registrados")
            } else {
                (if (verTodos) movimientos else movimientos.take(10)).forEach { movimiento ->
                    DatoDetalle(dato(movimiento.tipo), dato(movimiento.descripcion))
                    Text("Fecha: ${dato(movimiento.fecha)}")
                    Text("Lote: ${dato(movimiento.lote)}")
                    Text("Cantidad: ${movimiento.cantidad?.toString() ?: "No registrada"}")
                    Text("Responsable: ${dato(movimiento.responsable)}")
                    HorizontalDivider(color = FondoDetalle)
                }
            }
        }

        if (movimientos.size > 10) {
            TextButton(onClick = { verTodos = !verTodos }) {
                Text(if (verTodos) "Ver los diez más recientes" else "Ver todos los movimientos")
            }
        }
        Text("El registro de movimientos no modifica el stock del producto.", color = AzulDetalle,
            style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(8.dp))
    }
}

private fun dato(valor: String?): String = valor?.takeIf { it.isNotBlank() } ?: "No registrado"

@Composable
private fun BloqueDetalle(contenido: @Composable ColumnScope.() -> Unit) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp),
            content = contenido)
    }
}

@Composable
private fun DatoDetalle(etiqueta: String, valor: String, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(etiqueta, color = AzulDetalle, style = MaterialTheme.typography.labelMedium)
        Text(valor, color = AzulDetalle, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 850)
@Composable
private fun DetalleProductoPreview() {
    AplicacionUPC2749Theme {
        DetalleProductoContenido(
            uiState = DetalleProductoUiState(producto = ProductoDto(
                // Ejemplo del código fuente, solo para Preview.
                id = "PF-00482", codigo = "PF-00482",
                producto = "CEFTOXIZIMA 1 g Solución Inyectable", tipo = "PF",
                lote = "CT-8902A", vencimiento = "15/07/2027",
                ubicacion = "Anaquel A-04-N2", stock = 340, minimo = 100,
                unidad = "AMPOLLAS", estado = "Stock Óptimo (+240)"
            )), onVolver = {}, onReintentar = {}, onRegistrar = {}
        )
    }
}
