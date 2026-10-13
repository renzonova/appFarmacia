package com.jnarvaez.aplicacionupc2749.presentation.screens.movimientos

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jnarvaez.aplicacionupc2749.data.remote.ProductoDto
import com.jnarvaez.aplicacionupc2749.data.remote.MovimientoRequestDto

@Composable
fun RegistrarMovimientoScreen(
    producto: ProductoDto,
    sesion: String,
    onVolver: () -> Unit,
    viewModel: MovimientoViewModel = hiltViewModel(key = "registro-$sesion")
) {
    val estado by viewModel.uiState.collectAsState()
    var tipo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var lote by remember { mutableStateOf(producto.lote) }
    var cantidad by remember { mutableStateOf("") }
    var responsable by remember { mutableStateOf("") }
    var errorLocal by remember { mutableStateOf<String?>(null) }
    var enviado by remember { mutableStateOf(false) }
    LaunchedEffect(estado.guardado) { if (estado.guardado) onVolver() }
    BackHandler(enabled = !estado.guardando, onBack = onVolver)
    BackHandler(enabled = estado.guardando) { }
    Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)
        .verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Registrar movimiento", style = MaterialTheme.typography.titleLarge)
        Text(producto.producto)
        Text("Código: ${producto.codigo} · Stock: ${producto.stock} ${producto.unidad}")
        Text("Este formulario registra un movimiento; no modifica existencias.")
        OutlinedTextField(tipo, { tipo = it }, label = { Text("Tipo de movimiento") },
            enabled = !enviado, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(descripcion, { descripcion = it }, label = { Text("Descripción") },
            enabled = !enviado, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(lote, { lote = it }, label = { Text("Lote") },
            enabled = !enviado, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(cantidad, { cantidad = it }, label = { Text("Cantidad registrada") },
            enabled = !enviado, modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text))
        Text("Use el signo del registro: por ejemplo, ingreso +10 o salida -10.")
        OutlinedTextField(responsable, { responsable = it }, label = { Text("Responsable") },
            enabled = !enviado, modifier = Modifier.fillMaxWidth())
        (errorLocal ?: estado.error)?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button(onClick = {
            val n = cantidad.toIntOrNull()
            errorLocal = when {
                tipo.isBlank() || descripcion.isBlank() || lote.isBlank() || responsable.isBlank() -> "Complete todos los campos."
                n == null -> "Ingrese una cantidad entera."
                else -> null
            }
            if (errorLocal == null && n != null) {
                enviado = true
                viewModel.registrar(
                    producto.id,
                    MovimientoRequestDto(tipo.trim(), descripcion.trim(), lote.trim(), n, responsable.trim()),
                    productoNombre = producto.producto
                )
            }
        }, enabled = !enviado, modifier = Modifier.fillMaxWidth()) {
            Text(if (estado.guardando) "Guardando…" else "Guardar movimiento")
        }
        if (enviado && !estado.guardando && !estado.guardado) {
            Text("Consulte los movimientos antes de volver a registrar; la respuesta pudo haberse perdido.")
        }
        TextButton(onClick = onVolver, enabled = !estado.guardando) { Text("Volver al detalle") }
    }
}
