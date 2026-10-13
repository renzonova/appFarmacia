package com.jnarvaez.aplicacionupc2749.presentation.screens.inicio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ProductionQuantityLimits
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jnarvaez.aplicacionupc2749.presentation.screens.inventario.InventarioViewModel
import com.jnarvaez.aplicacionupc2749.presentation.screens.inventario.Producto
import java.text.NumberFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Locale

// Paleta de colores ajustada a la referencia de diseño
private val Surface = Color(0xFFF7F9FF)
private val SurfaceLowest = Color(0xFFFFFFFF)
private val SurfaceLow = Color(0xFFEDF4FF)
private val SurfaceContainer = Color(0xFFE4EFFD)
private val SurfaceHigh = Color(0xFFDFE9F7)
private val OnSurface = Color(0xFF121D26)
private val OnSurfaceVariant = Color(0xFF5A6E7F)
private val PrimaryContainer = Color(0xFF07566A)
private val Secondary = Color(0xFF1B6D24)
private val SecondaryContainer = Color(0xFFA0F399)
private val TertiaryContainer = Color(0xFF623E00)
private val OnTertiaryContainer = Color(0xFFF3A31F)
private val Error = Color(0xFFBA1A1A)
private val ErrorContainer = Color(0xFFFFDAD6)
private val OnErrorContainer = Color(0xFF93000A)

@Composable
fun DashboardInicioScreen(
    viewModel: InventarioViewModel = hiltViewModel(),
    onInventarioClick: () -> Unit = {},
    onEscanearClick: () -> Unit = {},
    onMovimientosClick: () -> Unit = {},
    onMasClick: () -> Unit = {},
    onVerDetalleProducto: (String) -> Unit = {}
) {
    val productos = viewModel.productos
    val cargando by viewModel.cargando.collectAsState()

    // 📌 Métricas dinámicas calculadas a partir del inventario en base de datos
    val cantidadMedicamentos = productos.size
    val totalMedicamentosTexto = if (cargando && productos.isEmpty()) {
        "..."
    } else {
        NumberFormat.getNumberInstance(Locale.US).format(cantidadMedicamentos)
    }

    val totalStockCalculado = productos.sumOf { it.stock }
    val totalStockTexto = NumberFormat.getNumberInstance(Locale.US).format(totalStockCalculado)

    val itemsCriticos = productos.filter { it.stock <= it.minimo }
    val cantidadBajoMinimo = itemsCriticos.size

    val hoy = LocalDate.now()
    val limiteVencimiento = hoy.plusDays(90)
    val itemsProximosVencer = productos.filter {
        try {
            !it.fechaVencimiento.isBefore(hoy) && it.fechaVencimiento.isBefore(limiteVencimiento)
        } catch (_: Exception) {
            false
        }
    }
    val cantidadProximosVencer = itemsProximosVencer.size

    // Producto prioritario FEFO (el más próximo a vencer del inventario)
    val productoMasProximo = productos
        .filter { !it.fechaVencimiento.isBefore(hoy) }
        .minByOrNull { it.fechaVencimiento }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
            .statusBarsPadding()
    ) {
        CabeceraFarmacia()

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(14.dp, 10.dp, 14.dp, 18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Estado y sincronización
            item {
                EstadoSincronizacion(
                    cargando = cargando,
                    onSincronizar = { viewModel.cargarProductosDesdeAWS() }
                )
            }

            // Guardia activa
            item { TurnoActivo() }

            // Alerta Cadena de frío
            item { AlertaCadenaFrio() }

            // Balance operativo (Existencias con cantidad de medicamentos, Mínimo, FEFO, Diferencias)
            item {
                BalanceOperativo(
                    totalMedicamentos = totalMedicamentosTexto,
                    totalStockUnidades = totalStockTexto,
                    stockMinimo = cantidadBajoMinimo,
                    proximosVencer = cantidadProximosVencer
                )
            }

            // Acciones inmediatas
            item {
                AccionesTurno(
                    onInventarioClick = onInventarioClick,
                    onEscanearClick = onEscanearClick,
                    onMovimientosClick = onMovimientosClick
                )
            }

            // Actividades prioritarias con datos reales del inventario
            item {
                ActividadesPrioritarias(
                    itemsCriticos = itemsCriticos,
                    productoFefo = productoMasProximo,
                    onAtenderReposicion = onInventarioClick,
                    onVerDetalleProducto = onVerDetalleProducto,
                    onIniciarConteo = onEscanearClick
                )
            }
        }

        BarraInferiorStitch(
            onInventarioClick = onInventarioClick,
            onEscanearClick = onEscanearClick,
            onMovimientosClick = onMovimientosClick,
            onMasClick = onMasClick
        )
    }
}

// =========================================================================
// 1. CABECERA
// =========================================================================

@Composable
private fun CabeceraFarmacia() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CajaIcono(Icons.Default.MedicalServices, 40.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "SERVICIO DE FARMACIA",
                    color = PrimaryContainer,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = "Inicio",
                    color = OnSurface,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier.size(44.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(SurfaceHigh, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Farmacéutico de guardia",
                        tint = PrimaryContainer,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset((-2).dp, (-2).dp)
                        .size(10.dp)
                        .background(Secondary, CircleShape)
                )
            }
        }
    }
}

// =========================================================================
// 2. TERMINAL OPERATIVO Y SINCRONIZACIÓN
// =========================================================================

@Composable
private fun EstadoSincronizacion(
    cargando: Boolean,
    onSincronizar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceLow, RoundedCornerShape(11.dp))
            .padding(start = 12.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(if (cargando) Color(0xFFF3A31F) else Secondary, CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "TERMINAL OPERATIVO",
                color = OnSurfaceVariant,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.6.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (cargando) "Sincronizando con AWS... " else "Sincronizado: hace 2 min ",
                    color = OnSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (cargando) "(Cargando)" else "(En línea)",
                    color = if (cargando) Color(0xFFF3A31F) else Secondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        IconButton(
            onClick = onSincronizar,
            modifier = Modifier
                .size(42.dp)
                .background(SurfaceContainer, RoundedCornerShape(8.dp))
        ) {
            Icon(
                imageVector = Icons.Default.Sync,
                contentDescription = "Sincronizar",
                tint = PrimaryContainer,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// =========================================================================
// 3. GUARDIA ACTIVA
// =========================================================================

@Composable
private fun TurnoActivo() {
    TarjetaBase {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CajaIcono(Icons.Default.MedicalServices, 40.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(Secondary, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "GUARDIA ACTIVA",
                        color = Secondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.6.sp
                    )
                }
                Text(
                    text = "Turno Mañana • 07:00 - 15:00",
                    color = OnSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Farmacia Central (Sede Principal - Nivel 1)",
                    color = OnSurfaceVariant,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column(horizontalAlignment = Alignment.End) {
                Etiqueta("QF Supervisor", SurfaceContainer, PrimaryContainer)
                Text(
                    text = "ID: QF-9482",
                    color = OnSurfaceVariant,
                    fontSize = 9.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

// =========================================================================
// 4. ALERTA CADENA DE FRÍO
// =========================================================================

@Composable
private fun AlertaCadenaFrio() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(11.dp),
        colors = CardDefaults.cardColors(containerColor = ErrorContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AcUnit,
                    contentDescription = null,
                    tint = Error,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "ALERTA CADENA DE FRÍO",
                    color = Error,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.weight(1f)
                )
                Etiqueta("15 MIN CRÍTICO", Error, Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Refrigerador Nº 2 (Biológicos / Vacunas)",
                        color = OnErrorContainer,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Rango normativo permitido: 2.0 °C a 8.0 °C",
                        color = OnErrorContainer,
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "8.4 °C",
                        color = Error,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WarningAmber,
                            contentDescription = null,
                            tint = Color(0xFFF9A825),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "+0.4°C DESVÍO",
                            color = Error,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(7.dp))
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Sensors,
                    contentDescription = null,
                    tint = OnSurfaceVariant,
                    modifier = Modifier.size(17.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Sensor TH-RF02 • Último pulso: 08:29 AM",
                    color = OnSurface,
                    fontSize = 10.sp,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Error),
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(
                        text = "Ver Incidencia",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }
    }
}

// =========================================================================
// 5. BALANCE OPERATIVO INMEDIATO (Métricas del Inventario)
// =========================================================================

@Composable
private fun BalanceOperativo(
    totalMedicamentos: String,
    totalStockUnidades: String,
    stockMinimo: Int,
    proximosVencer: Int
) {
    Column {
        TituloSeccion("BALANCE OPERATIVO INMEDIATO", "Cierre parcial 08:30 AM")
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IndicadorCard(
                modifier = Modifier.weight(1f),
                icono = Icons.Default.Inventory2,
                fondoIcono = SurfaceLow,
                colorIcono = PrimaryContainer,
                etiquetaSuperior = "99.4% Op.",
                colorEtiqueta = Secondary,
                fondoEtiqueta = Color.Transparent,
                titulo = "TOTAL EXISTENCIAS",
                valor = totalMedicamentos,
                unidad = "medicamentos",
                detalle = "$totalStockUnidades unidades en stock (PF • DM • PS)",
                colorValor = OnSurface
            )
            IndicadorCard(
                modifier = Modifier.weight(1f),
                icono = Icons.Default.ProductionQuantityLimits,
                fondoIcono = Color(0xFFFFDDB5),
                colorIcono = TertiaryContainer,
                etiquetaSuperior = "Crítico",
                colorEtiqueta = OnTertiaryContainer,
                fondoEtiqueta = TertiaryContainer,
                titulo = "STOCK MÍNIMO",
                valor = stockMinimo.toString(),
                unidad = "items",
                detalle = "UCI, Emergencia y Quirófano",
                colorValor = TertiaryContainer
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IndicadorCard(
                modifier = Modifier.weight(1f),
                icono = Icons.Default.EventBusy,
                fondoIcono = ErrorContainer,
                colorIcono = OnErrorContainer,
                etiquetaSuperior = "FEFO",
                colorEtiqueta = Color.White,
                fondoEtiqueta = Error,
                titulo = "PRÓX. VENCER",
                valor = proximosVencer.toString(),
                unidad = "lotes",
                detalle = "Ventana <60 días naturales",
                colorValor = Error
            )
            IndicadorCard(
                modifier = Modifier.weight(1f),
                icono = Icons.AutoMirrored.Filled.FactCheck,
                fondoIcono = SurfaceHigh,
                colorIcono = PrimaryContainer,
                etiquetaSuperior = "Auditoría",
                colorEtiqueta = Color.White,
                fondoEtiqueta = PrimaryContainer,
                titulo = "DIFERENCIAS",
                valor = "3",
                unidad = "casos",
                detalle = "En revisión por Químico",
                colorValor = PrimaryContainer
            )
        }
    }
}

@Composable
private fun IndicadorCard(
    modifier: Modifier,
    icono: ImageVector,
    fondoIcono: Color,
    colorIcono: Color,
    etiquetaSuperior: String,
    colorEtiqueta: Color,
    fondoEtiqueta: Color,
    titulo: String,
    valor: String,
    unidad: String,
    detalle: String,
    colorValor: Color
) {
    Card(
        modifier = modifier.height(134.dp),
        shape = RoundedCornerShape(9.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLowest),
        border = BorderStroke(0.5.dp, SurfaceHigh),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(11.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(fondoIcono, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = colorIcono,
                        modifier = Modifier.size(19.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = etiquetaSuperior,
                    color = colorEtiqueta,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(fondoEtiqueta, RoundedCornerShape(3.dp))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                )
            }
            Column {
                Text(
                    text = titulo,
                    color = OnSurfaceVariant,
                    fontSize = 10.sp,
                    letterSpacing = 0.5.sp
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = valor,
                        color = colorValor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (unidad.isNotBlank()) {
                        Text(
                            text = " $unidad",
                            color = OnSurfaceVariant,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(bottom = 3.dp)
                        )
                    }
                }
                Text(
                    text = detalle,
                    color = OnSurfaceVariant,
                    fontSize = 10.sp,
                    lineHeight = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// =========================================================================
// 6. ACCIONES INMEDIATAS DE TURNO
// =========================================================================

@Composable
private fun AccionesTurno(
    onInventarioClick: () -> Unit,
    onEscanearClick: () -> Unit,
    onMovimientosClick: () -> Unit = {}
) {
    Column {
        TituloSeccion("ACCIONES INMEDIATAS DE TURNO", "Flujo guiado")
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AccionCard(
                modifier = Modifier.weight(1f),
                icono = Icons.Default.Inventory,
                titulo = "Recepción",
                descripcion = "Ingreso bultos y guías",
                onClick = onEscanearClick
            )
            AccionCard(
                modifier = Modifier.weight(1f),
                icono = Icons.Default.Checklist,
                titulo = "Conteo Rápido",
                descripcion = "Anaquel vs Sistema",
                onClick = onInventarioClick
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AccionCard(
                modifier = Modifier.weight(1f),
                icono = Icons.Default.SwapHoriz,
                titulo = "Mover Stock",
                descripcion = "Reubicación y traslado",
                onClick = onMovimientosClick
            )
            AccionCard(
                modifier = Modifier.weight(1f),
                icono = Icons.Default.Thermostat,
                titulo = "Control Ambiental",
                descripcion = "Registro T° y Humedad"
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        TarjetaBase(
            modifier = Modifier.clickable(onClick = onInventarioClick),
            padding = 11.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CajaIcono(Icons.Default.Emergency, 40.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Kits y Coches de Paro",
                        color = OnSurface,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Shock Trauma, Quirófano y Botiquines",
                        color = OnSurfaceVariant,
                        fontSize = 10.sp
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = OnSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun AccionCard(
    modifier: Modifier,
    icono: ImageVector,
    titulo: String,
    descripcion: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .height(104.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(9.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLowest),
        border = BorderStroke(0.5.dp, SurfaceHigh),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(11.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            CajaIcono(icono, 38.dp)
            Column {
                Text(
                    text = titulo,
                    color = OnSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = descripcion,
                    color = OnSurfaceVariant,
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// =========================================================================
// 7. ACTIVIDADES PRIORITARIAS (DATOS DINÁMICOS DE MEDICAMENTOS)
// =========================================================================

@Composable
private fun ActividadesPrioritarias(
    itemsCriticos: List<Producto>,
    productoFefo: Producto?,
    onAtenderReposicion: () -> Unit,
    onVerDetalleProducto: (String) -> Unit,
    onIniciarConteo: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Flag,
                contentDescription = null,
                tint = PrimaryContainer,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "ACTIVIDADES PRIORITARIAS DEL TURNO",
                color = OnSurface,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                modifier = Modifier.weight(1f)
            )
            Etiqueta("1 de 3 listas", SecondaryContainer, Secondary)
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Tarjeta 1: Reposición Crítica (dinámica según inventario bajo mínimo)
        ActividadReposicion(
            itemsCriticos = itemsCriticos,
            onAtender = onAtenderReposicion
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tarjeta 2: Urgente FEFO (dinámica según el medicamento más próximo a vencer)
        ActividadFefo(
            producto = productoFefo,
            onVerDetalle = onVerDetalleProducto
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tarjeta 3: Recepción
        ActividadRecepcion(onIniciarConteo = onIniciarConteo)
    }
}

@Composable
private fun ActividadReposicion(
    itemsCriticos: List<Producto>,
    onAtender: () -> Unit
) {
    val detalleMedicamentos = if (itemsCriticos.isNotEmpty()) {
        itemsCriticos.take(3).joinToString(", ") { "${it.producto} (${it.stock} uds)" }
    } else {
        "Adrenalina 1mg/ml (15 uds), Atropina 1mg (10 uds), Fentanilo 0.5mg (8 amp)."
    }

    TarjetaBase {
        FilaEtiquetas(
            etiqueta = "PRIORITARIO",
            fondoEtiqueta = TertiaryContainer,
            colorEtiqueta = OnTertiaryContainer,
            codigo = "REQ-ST-0412",
            estado = "Pendiente",
            colorEstado = TertiaryContainer
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Reposición Crítica: Shock Trauma y UCI",
            color = OnSurface,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = detalleMedicamentos,
            color = OnSurfaceVariant,
            fontSize = 10.5.sp,
            lineHeight = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        PieActividad(
            texto = "Destino: Piso 2 - Botiquín A",
            boton = "Atender",
            botonPrincipal = true,
            onClick = onAtender
        )
    }
}

@Composable
private fun ActividadFefo(
    producto: Producto?,
    onVerDetalle: (String) -> Unit
) {
    // Si hay producto real del inventario se muestra dinámicamente; si no, datos de muestra
    val nombre = producto?.producto ?: "Meropenem 1g Polvo p/ Solución"
    val tipo = producto?.let { "${it.tipo} • ${it.unidad}" } ?: "PF • Inyectable"
    val stockTexto = producto?.let { "${it.stock} ${it.unidad.lowercase().replaceFirstChar { c -> c.uppercase() }}" } ?: "45 Fcos"
    val loteTexto = producto?.let { "Lote: ${it.lote}" } ?: "Lote: MP-2023B"
    val venceTexto = producto?.let { "Vence: ${it.vencimiento}" } ?: "Vence: 28/OCT/2026"
    val ubicacionTexto = producto?.let { "Ubicación: ${it.ubicacion}" } ?: "Ubicación: PAS-03 / EST-01 / NIV-C"

    TarjetaBase {
        FilaEtiquetas(
            etiqueta = "URGENTE FEFO",
            fondoEtiqueta = Error,
            colorEtiqueta = Color.White,
            codigo = tipo,
            estado = "Próximo a vencer",
            colorEstado = Error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = nombre,
                color = OnSurface,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stockTexto,
                color = OnSurface,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Etiqueta(loteTexto, SurfaceContainer, OnSurface)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = venceTexto,
                color = Error,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        PieActividad(
            texto = ubicacionTexto,
            boton = "Ver Detalle",
            botonPrincipal = true,
            onClick = {
                producto?.let { onVerDetalle(it.codigo) }
            }
        )
    }
}

@Composable
private fun ActividadRecepcion(onIniciarConteo: () -> Unit) {
    TarjetaBase {
        FilaEtiquetas(
            etiqueta = "EN ESPERA",
            fondoEtiqueta = SurfaceHigh,
            colorEtiqueta = OnSurfaceVariant,
            codigo = "GR-8492",
            estado = "Muelle de Carga",
            colorEstado = OnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Recepción de Droguería Central",
            color = OnSurface,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "12 bultos termolábiles e insumos descartables pendientes de cotejo físico.",
            color = OnSurfaceVariant,
            fontSize = 10.5.sp,
            lineHeight = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        PieActividad(
            texto = "Transporte: FrioExpress #T-29",
            boton = "Iniciar Conteo",
            botonPrincipal = false,
            onClick = onIniciarConteo
        )
    }
}

// =========================================================================
// 8. HELPERS VISUALES
// =========================================================================

@Composable
private fun FilaEtiquetas(
    etiqueta: String,
    fondoEtiqueta: Color,
    colorEtiqueta: Color,
    codigo: String,
    estado: String,
    colorEstado: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Etiqueta(etiqueta, fondoEtiqueta, colorEtiqueta)
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = codigo,
            color = OnSurfaceVariant,
            fontSize = 9.5.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = estado,
            color = colorEstado,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun PieActividad(
    texto: String,
    boton: String,
    botonPrincipal: Boolean,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceLow, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = OnSurfaceVariant,
            modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = texto,
            color = OnSurfaceVariant,
            fontSize = 9.5.sp,
            lineHeight = 12.sp,
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (botonPrincipal) PrimaryContainer else SurfaceContainer,
                contentColor = if (botonPrincipal) Color.White else PrimaryContainer
            ),
            contentPadding = PaddingValues(horizontal = 10.dp),
            modifier = Modifier.height(34.dp)
        ) {
            Text(
                text = boton,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// =========================================================================
// 9. BARRA DE NAVEGACIÓN INFERIOR
// =========================================================================

@Composable
private fun BarraInferiorStitch(
    onInventarioClick: () -> Unit,
    onEscanearClick: () -> Unit,
    onMovimientosClick: () -> Unit = {},
    onMasClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceLowest)
            .navigationBarsPadding()
            .height(70.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ItemBarra(Icons.Default.Home, "Inicio", seleccionado = true)
            ItemBarra(Icons.Default.Inventory2, "Inventario", onClick = onInventarioClick)
            Spacer(modifier = Modifier.width(64.dp))
            ItemBarra(Icons.Default.SwapHoriz, "Movimientos", onClick = onMovimientosClick)
            ItemBarra(Icons.Default.GridView, "Más", onClick = onMasClick)
        }
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-14).dp)
                .clickable(onClick = onEscanearClick),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = PrimaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "Escanear código",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Text(
                text = "Escanear",
                color = PrimaryContainer,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ItemBarra(
    icono: ImageVector,
    texto: String,
    seleccionado: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .width(58.dp)
            .height(52.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icono,
            contentDescription = texto,
            tint = if (seleccionado) PrimaryContainer else OnSurfaceVariant,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = texto,
            color = if (seleccionado) PrimaryContainer else OnSurfaceVariant,
            fontSize = 9.sp,
            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1
        )
    }
}

@Composable
private fun TarjetaBase(
    modifier: Modifier = Modifier,
    padding: Dp = 11.dp,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(9.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLowest),
        border = BorderStroke(0.5.dp, SurfaceHigh),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Box(modifier = Modifier.padding(padding)) { content() }
    }
}

@Composable
private fun CajaIcono(icono: ImageVector, tamano: Dp) {
    Box(
        modifier = Modifier
            .size(tamano)
            .background(PrimaryContainer, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun TituloSeccion(titulo: String, textoDerecha: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titulo,
            color = OnSurfaceVariant,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = textoDerecha,
            color = OnSurfaceVariant,
            fontSize = 9.5.sp
        )
    }
}

@Composable
private fun Etiqueta(texto: String, fondo: Color, color: Color) {
    Text(
        text = texto,
        color = color,
        fontSize = 8.5.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 10.sp,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(fondo)
            .padding(horizontal = 6.dp, vertical = 3.dp)
    )
}
