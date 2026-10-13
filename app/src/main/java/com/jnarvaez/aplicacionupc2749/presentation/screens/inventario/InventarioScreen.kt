package com.jnarvaez.aplicacionupc2749.presentation.screens.inventario

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.statusBarsPadding
import com.jnarvaez.aplicacionupc2749.presentation.common.EstadoStock
import com.jnarvaez.aplicacionupc2749.presentation.common.FiltroChip
import com.jnarvaez.aplicacionupc2749.presentation.screens.form.FormScreen
import com.jnarvaez.aplicacionupc2749.presentation.screens.movimientos.MovimientosScreen
import com.jnarvaez.aplicacionupc2749.presentation.screens.mas.MasScreen
import java.time.LocalDate
import androidx.compose.runtime.saveable.rememberSaveable
import com.jnarvaez.aplicacionupc2749.presentation.screens.detalleproducto.DetalleProductoScreen


import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun InventarioScreen(
    viewModel: InventarioViewModel = hiltViewModel(),
    pantallaInicial: String = "inventario",
    codigoInicialDetalle: String? = null,
    onInicioClick: () -> Unit = {},
    onCerrarSesion: () -> Unit = {}
) {
    var codigoDetalleProducto by rememberSaveable(codigoInicialDetalle) { mutableStateOf(codigoInicialDetalle) }
    val context = LocalContext.current
    val mensajeNotificacion by viewModel.mensajeNotificacion.collectAsState()

    LaunchedEffect(mensajeNotificacion) {
        mensajeNotificacion?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.resetNotificacion()
        }
    }

    // =====================================================
    // VISTA ACTUAL
    // =====================================================

    var vistaActual by remember(pantallaInicial) {
        mutableStateOf(pantallaInicial)
    }

    // =====================================================
    // PRODUCTO QUE SE ESTÁ EDITANDO
    // =====================================================

    var productoEditando by remember {
        mutableStateOf<Producto?>(null)
    }

    // =====================================================
    // BUSCADOR
    // =====================================================

    var busqueda by remember {
        mutableStateOf("")
    }

    // =====================================================
    // FILTRO DE TIPO
    // =====================================================

    var tipoSeleccionado by remember {
        mutableStateOf("Todos")
    }

    // =====================================================
    // FILTRO DE ESTADO
    // =====================================================

    var estadoSeleccionado by remember {
        mutableStateOf("Todos")
    }

    // =====================================================
    // ORDEN
    // =====================================================

    var ordenSeleccionado by remember {
        mutableStateOf("Vencimiento (FEFO)")
    }

    var menuOrdenAbierto by remember {
        mutableStateOf(false)
    }

    // =====================================================
    // PRODUCTOS
    // =====================================================

    val productos = viewModel.productos
    /*
    val productosLegacy = remember {

        mutableStateListOf(

            // ==========================
            // FÁRMACOS
            // ==========================

            Producto(
                tipo = "PF",
                codigo = "PF-00482",
                producto = "CEFTOXIZIMA 1 g Solución Inyectable",
                lote = "CT-8902A",
                vencimiento = "15/07/2027",
                fechaVencimiento = LocalDate.of(2027, 7, 15),
                ubicacion = "Anaquel A-04-N2",
                minimo = 100,
                stock = 340,
                unidad = "AMPOLLAS",
                estado = "Stock Óptimo (+240)",
                estadoTipo = "normal",
                accion = "Kárdex"
            ),

           /* Producto(
                tipo = "PF",
                codigo = "PF-00109",
                producto = "INSULINA NPH HUMANA 100 UI/ml Suspensión Iny.",
                lote = "IN-4022",
                vencimiento = "28/11/2026",
                fechaVencimiento = LocalDate.of(2026, 11, 28),
                ubicacion = "Refrigerador RF-01-B",
                minimo = 30,
                stock = 18,
                unidad = "VIALES",
                estado = "Bajo Mínimo: Faltan 12",
                estadoTipo = "bajo",
                accion = "Reponer"
            ),

            Producto(
                tipo = "PF",
                codigo = "PF-00631",
                producto = "AMOXICILINA 500 mg Cápsulas",
                lote = "AM-7712",
                vencimiento = "10/12/2026",
                fechaVencimiento = LocalDate.of(2026, 12, 10),
                ubicacion = "Anaquel A-02-N1",
                minimo = 80,
                stock = 115,
                unidad = "CÁPSULAS",
                estado = "Próximo a Vencer",
                estadoTipo = "alerta",
                accion = "Revisar"
            ),

            Producto(
                tipo = "PF",
                codigo = "PF-00820",
                producto = "PARACETAMOL 500 mg Tabletas",
                lote = "PA-5520",
                vencimiento = "05/03/2028",
                fechaVencimiento = LocalDate.of(2028, 3, 5),
                ubicacion = "Anaquel A-06-N3",
                minimo = 100,
                stock = 560,
                unidad = "TABLETAS",
                estado = "Stock Óptimo (+460)",
                estadoTipo = "normal",
                accion = "Kárdex"
            ),

            // ==========================
            // DISPOSITIVOS MÉDICOS
            // ==========================

            Producto(
                tipo = "DM",
                codigo = "DM-00951",
                producto = "CATÉTER INTRAVENOSO PERIFÉRICO N° 20G con Teflón",
                lote = "CT-2024-X",
                vencimiento = "10/11/2027",
                fechaVencimiento = LocalDate.of(2027, 11, 10),
                ubicacion = "Gaveta G-12-C",
                minimo = 150,
                stock = 520,
                unidad = "UNIDADES",
                estado = "Conforme (+370)",
                estadoTipo = "normal",
                accion = "Conteo Físico"
            ),

            Producto(
                tipo = "DM",
                codigo = "DM-00214",
                producto = "JERINGA DESCARTABLE 10 ml",
                lote = "JD-4401",
                vencimiento = "20/01/2027",
                fechaVencimiento = LocalDate.of(2027, 1, 20),
                ubicacion = "Gaveta G-04-A",
                minimo = 200,
                stock = 145,
                unidad = "UNIDADES",
                estado = "Bajo Mínimo: Faltan 55",
                estadoTipo = "bajo",
                accion = "Reponer"
            ),

            Producto(
                tipo = "DM",
                codigo = "DM-00573",
                producto = "GUANTES QUIRÚRGICOS ESTÉRILES Talla M",
                lote = "GQ-8830",
                vencimiento = "18/10/2026",
                fechaVencimiento = LocalDate.of(2026, 10, 18),
                ubicacion = "Gaveta G-08-B",
                minimo = 100,
                stock = 210,
                unidad = "PARES",
                estado = "Próximo a Vencer",
                estadoTipo = "alerta",
                accion = "Revisar"
            ),

            Producto(
                tipo = "DM",
                codigo = "DM-00742",
                producto = "EQUIPO DE INFUSIÓN MACROGOTERO",
                lote = "EI-3321",
                vencimiento = "22/09/2028",
                fechaVencimiento = LocalDate.of(2028, 9, 22),
                ubicacion = "Gaveta G-15-D",
                minimo = 100,
                stock = 385,
                unidad = "UNIDADES",
                estado = "Conforme (+285)",
                estadoTipo = "normal",
                accion = "Conteo Físico"
            ),

            // ==========================
            // PRODUCTOS SANITARIOS
            // ==========================

            Producto(
                tipo = "PS",
                codigo = "PS-00311",
                producto = "CLORHEXIDINA GLUCONATO 4% Solución Jabonosa 1L",
                lote = "CH-0911",
                vencimiento = "30/08/2027",
                fechaVencimiento = LocalDate.of(2027, 8, 30),
                ubicacion = "Estante E-02-Piso",
                minimo = 20,
                stock = 45,
                unidad = "FRASCOS",
                estado = "Stock Regular (+25)",
                estadoTipo = "normal",
                accion = "Reubicar"
            ),

            Producto(
                tipo = "PS",
                codigo = "PS-00418",
                producto = "ALCOHOL ANTISÉPTICO 70% 1L",
                lote = "AL-7720",
                vencimiento = "14/02/2027",
                fechaVencimiento = LocalDate.of(2027, 2, 14),
                ubicacion = "Estante E-03-Piso",
                minimo = 30,
                stock = 18,
                unidad = "FRASCOS",
                estado = "Bajo Mínimo: Faltan 12",
                estadoTipo = "bajo",
                accion = "Reponer"
            ),

            Producto(
                tipo = "PS",
                codigo = "PS-00652",
                producto = "GASA ESTÉRIL 10 x 10 cm",
                lote = "GS-2209",
                vencimiento = "25/12/2026",
                fechaVencimiento = LocalDate.of(2026, 12, 25),
                ubicacion = "Estante E-05-Piso",
                minimo = 50,
                stock = 75,
                unidad = "PAQUETES",
                estado = "Próximo a Vencer",
                estadoTipo = "alerta",
                accion = "Revisar"
            ),

            Producto(
                tipo = "PS",
                codigo = "PS-00801",
                producto = "POVIDONA YODADA 10% Solución 1L",
                lote = "PY-6512",
                vencimiento = "30/01/2028",
                fechaVencimiento = LocalDate.of(2028, 1, 30),
                ubicacion = "Estante E-07-Piso",
                minimo = 20,
                stock = 68,
                unidad = "FRASCOS",
                estado = "Stock Óptimo (+48)",
                estadoTipo = "normal",
                accion = "Reubicar"
            )*/
        )
    }
    */


    // =====================================================
    // EDITAR PRODUCTO
    // =====================================================

    if (codigoDetalleProducto != null) {
        DetalleProductoScreen(
            codigo = codigoDetalleProducto!!,
            onVolver = {
                codigoDetalleProducto = null
                viewModel.cargarProductosDesdeAWS()
            }
        )
        return
    }

    if (productoEditando != null) {

        EditarProductoScreen(
            producto = productoEditando!!,

            onGuardar = { productoActualizado ->
                val codigoOriginal = productoEditando!!.codigo
                val posicion = productos.indexOfFirst {
                    it.codigo == codigoOriginal
                }

                if (posicion >= 0) {
                    productos[posicion] = productoActualizado
                }

                // Sincronizar automáticamente con AWS DynamoDB y registrar en movimientos
                viewModel.actualizarProductoEnAWS(productoActualizado, codigoOriginal = codigoOriginal)

                productoEditando = null
            },

            onEliminar = { codigoAEliminar ->
                viewModel.eliminarProductoEnAWS(codigoAEliminar)
                productoEditando = null
            },

            onCancelar = {
                productoEditando = null
            }
        )

        return
    }


    // =====================================================
    // CREAR NUEVO MEDICAMENTO / PRODUCTO
    // =====================================================

    if (vistaActual == "crear") {
        FormScreen(
            onGuardar = { nuevoProducto ->
                productos.add(0, nuevoProducto)
                viewModel.guardarProductoEnAWS(nuevoProducto)
                vistaActual = "inventario"
            },
            onCancelar = {
                vistaActual = "inventario"
            }
        )

        return
    }


    // =====================================================
    // ESCANEAR
    // =====================================================

    if (vistaActual == "escanear") {

        EscanearScreen(
            onVolver = {
                vistaActual = "inventario"
            },

            onConsultarStock = {
                vistaActual = "inventario"
            }
        )

        return
    }


    // =====================================================
    // MOVIMIENTOS DE INVENTARIO
    // =====================================================

    if (vistaActual == "movimientos") {
        MovimientosScreen(
            movimientos = viewModel.movimientos,
            onVolver = {
                vistaActual = "inventario"
            }
        )
        return
    }


    // =====================================================
    // MÁS / INFORMACIÓN DE LA APLICACIÓN
    // =====================================================

    if (vistaActual == "mas") {
        MasScreen(
            onVolver = {
                vistaActual = "inventario"
            },
            onCerrarSesion = onCerrarSesion
        )
        return
    }


    // =====================================================
    // FILTROS Y ORDEN
    // =====================================================

    val productosFiltrados = productos
        .filter { producto ->

            val coincideTipo =
                tipoSeleccionado == "Todos" ||
                        producto.tipo == tipoSeleccionado

            val coincideEstado =
                estadoSeleccionado == "Todos" ||
                        producto.estadoTipo == estadoSeleccionado

            val coincideBusqueda =
                busqueda.isBlank() ||
                        producto.codigo.contains(
                            busqueda,
                            ignoreCase = true
                        ) ||
                        producto.producto.contains(
                            busqueda,
                            ignoreCase = true
                        ) ||
                        producto.lote.contains(
                            busqueda,
                            ignoreCase = true
                        )

            coincideTipo &&
                    coincideEstado &&
                    coincideBusqueda
        }
        .let { lista ->

            when (ordenSeleccionado) {

                "Vencimiento (FEFO)" ->
                    lista.sortedBy {
                        it.fechaVencimiento
                    }

                "Código" ->
                    lista.sortedBy {
                        it.codigo
                    }

                "Stock mayor" ->
                    lista.sortedByDescending {
                        it.stock
                    }

                else ->
                    lista
            }
        }


    // =====================================================
    // PANTALLA
    // =====================================================

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F8FC))
            .statusBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 18.dp)
        ) {

            Spacer(
                modifier = Modifier.height(16.dp)
            )


            // ENCABEZADO

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            Color(0xFF07566A),
                            RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "SERVICIO DE FARMACIA",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF19465A)
                    )

                    Text(
                        text = "Inventario",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF101820)
                    )
                }

                Button(
                    onClick = { viewModel.cargarProductosDesdeAWS() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF19465A)
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Text(
                        text = "🔄",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = { vistaActual = "crear" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF07566A)
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = "+ Nuevo",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Color(0xFFDCE7EF),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "👤",
                        fontSize = 20.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )


            // BUSCADOR

            OutlinedTextField(
                value = busqueda,
                onValueChange = {
                    busqueda = it
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Buscar código, DCI o lote..."
                    )
                },
                leadingIcon = {

                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFF07566A)
                    )
                },
                trailingIcon = {

                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = null,
                        tint = Color(0xFF07566A)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFF07566A),
                    unfocusedIndicatorColor = Color(0xFFD5DEE5)
                )
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )


            // TIPO

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "TIPO:",
                    fontSize = 11.sp,
                    color = Color(0xFF4E5B63)
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    item {

                        FiltroChip(
                            texto = "Todos",
                            seleccionado =
                                tipoSeleccionado == "Todos",
                            onClick = {
                                tipoSeleccionado = "Todos"
                            }
                        )
                    }

                    item {

                        FiltroChip(
                            texto = "PF: Fármacos",
                            seleccionado =
                                tipoSeleccionado == "PF",
                            onClick = {
                                tipoSeleccionado = "PF"
                            }
                        )
                    }

                    item {

                        FiltroChip(
                            texto = "DM: Disp. Médicos",
                            seleccionado =
                                tipoSeleccionado == "DM",
                            onClick = {
                                tipoSeleccionado = "DM"
                            }
                        )
                    }

                    item {

                        FiltroChip(
                            texto = "PS: Sanitarios",
                            seleccionado =
                                tipoSeleccionado == "PS",
                            onClick = {
                                tipoSeleccionado = "PS"
                            }
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )


            // ESTADO

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "ESTADO:",
                    fontSize = 11.sp,
                    color = Color(0xFF4E5B63)
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    item {

                        FiltroChip(
                            texto = "Todos",
                            seleccionado =
                                estadoSeleccionado == "Todos",
                            onClick = {
                                estadoSeleccionado = "Todos"
                            }
                        )
                    }

                    item {

                        EstadoStock(
                            texto = "Normal",
                            tipo = "normal",
                            seleccionado =
                                estadoSeleccionado == "normal",
                            onClick = {
                                estadoSeleccionado = "normal"
                            }
                        )
                    }

                    item {

                        EstadoStock(
                            texto = "Bajo Mínimo",
                            tipo = "bajo",
                            seleccionado =
                                estadoSeleccionado == "bajo",
                            onClick = {
                                estadoSeleccionado = "bajo"
                            }
                        )
                    }

                    item {

                        EstadoStock(
                            texto = "Próximo a Vencer",
                            tipo = "alerta",
                            seleccionado =
                                estadoSeleccionado == "alerta",
                            onClick = {
                                estadoSeleccionado = "alerta"
                            }
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )


            // ORDEN

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Box {

                    TextButton(
                        onClick = {
                            menuOrdenAbierto = true
                        }
                    ) {

                        Text(
                            text = "Orden: $ordenSeleccionado ▼",
                            color = Color(0xFF07566A),
                            fontSize = 12.sp
                        )
                    }

                    DropdownMenu(
                        expanded = menuOrdenAbierto,
                        onDismissRequest = {
                            menuOrdenAbierto = false
                        }
                    ) {

                        DropdownMenuItem(
                            text = {
                                Text("Vencimiento (FEFO)")
                            },
                            onClick = {
                                ordenSeleccionado =
                                    "Vencimiento (FEFO)"
                                menuOrdenAbierto = false
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text("Código")
                            },
                            onClick = {
                                ordenSeleccionado = "Código"
                                menuOrdenAbierto = false
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text("Stock mayor")
                            },
                            onClick = {
                                ordenSeleccionado = "Stock mayor"
                                menuOrdenAbierto = false
                            }
                        )
                    }
                }

                Text(
                    text = "${productosFiltrados.size} items",
                    fontSize = 12.sp,
                    color = Color(0xFF65727A)
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )


            // PRODUCTOS

            productosFiltrados.forEach { producto ->

                InventarioProductoCard(
                    tipo = producto.tipo,
                    codigo = producto.codigo,
                    producto = producto.producto,
                    lote = producto.lote,
                    vencimiento = producto.vencimiento,
                    ubicacion = producto.ubicacion,
                    minimo = producto.minimo,
                    stock = producto.stock,
                    unidad = producto.unidad,
                    estado = producto.estado,
                    estadoTipo = producto.estadoTipo,
                    accion = producto.accion,

                    onAccionClick = {

                        productoEditando = producto
                    }
                )

                //Boton para llamar a detalle productos
                TextButton(
                    onClick = { codigoDetalleProducto = producto.codigo },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver detalle", color = Color(0xFF07566A))
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(80.dp)
            )
        }


        // =================================================
        // ESCANEO CONTINUO
        // =================================================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 8.dp
                ),
            contentAlignment = Alignment.CenterEnd
        ) {

            Button(
                onClick = {
                    vistaActual = "escanear"
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF07566A)
                )
            ) {

                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "Escaneo Continuo"
                )
            }
        }


        // =================================================
        // BARRA INFERIOR
        // =================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(
                    horizontal = 8.dp,
                    vertical = 8.dp
                ),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            ItemNavegacion(
                icono = Icons.Default.Home,
                texto = "Inicio",
                seleccionado = false,
                onClick = {
                    onInicioClick()
                }
            )

            ItemNavegacion(
                icono = Icons.Default.Inventory2,
                texto = "Inventario",
                seleccionado = true,
                onClick = {
                    vistaActual = "inventario"
                }
            )

            ItemNavegacion(
                icono = Icons.Default.QrCodeScanner,
                texto = "Escanear",
                seleccionado = false,
                onClick = {
                    vistaActual = "escanear"
                }
            )

            ItemNavegacion(
                icono = Icons.Default.SwapHoriz,
                texto = "Movimientos",
                seleccionado = false,
                onClick = {
                    vistaActual = "movimientos"
                }
            )

            ItemNavegacion(
                icono = Icons.Default.GridView,
                texto = "Más",
                seleccionado = false,
                onClick = {
                    vistaActual = "mas"
                }
            )
        }
    }
}


// =====================================================
// MODELO DEL PRODUCTO
// =====================================================

data class Producto(
    val tipo: String,
    val codigo: String,
    val producto: String,
    val lote: String,
    val vencimiento: String,
    val fechaVencimiento: LocalDate,
    val ubicacion: String,
    val minimo: Int,
    val stock: Int,
    val unidad: String,
    val estado: String,
    val estadoTipo: String,
    val accion: String
)


// =====================================================
// NAVEGACIÓN INFERIOR
// =====================================================

@Composable
fun ItemNavegacion(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    texto: String,
    seleccionado: Boolean = false,
    onClick: () -> Unit = {}
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clickable {
                onClick()
            }
            .background(
                if (seleccionado) {
                    Color(0xFFE6F3F6)
                } else {
                    Color.Transparent
                },
                RoundedCornerShape(8.dp)
            )
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            )
    ) {

        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = if (seleccionado) {
                Color(0xFF07566A)
            } else {
                Color(0xFF455A64)
            },
            modifier = Modifier.size(22.dp)
        )

        Text(
            text = texto,
            fontSize = 10.sp,
            fontWeight = if (seleccionado) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            },
            color = if (seleccionado) {
                Color(0xFF07566A)
            } else {
                Color(0xFF455A64)
            }
        )
    }
}