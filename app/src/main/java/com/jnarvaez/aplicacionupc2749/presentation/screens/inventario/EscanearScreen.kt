package com.jnarvaez.aplicacionupc2749.presentation.screens.inventario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AzulEscaner = Color(0xFF07566A)
private val FondoEscaner = Color(0xFFF4F7FD)

@Composable
fun EscanearScreen(
    onVolver: () -> Unit = {},
    onConsultarStock: () -> Unit = {}
) {
    var modoQR by remember { mutableStateOf(false) }
    var luzActiva by remember { mutableStateOf(false) }
    var codigoManual by remember { mutableStateOf("") }
    var codigoDetectado by remember {
        mutableStateOf("7750123984120")
    }
    var bultos by remember { mutableIntStateOf(4) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoEscaner)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        // ENCABEZADO
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = AzulEscaner
                )
            }

            Spacer(Modifier.width(8.dp))

            Text(
                text = "Escanear",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(18.dp))

        // MÓDULO HOSPITALARIO
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "🛡 MÓDULO ÓPTICO HOSPITALARIO",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "$bultos / 12 bultos",
                fontSize = 12.sp,
                color = AzulEscaner
            )
        }

        Spacer(Modifier.height(12.dp))

        // ÁREA VISUAL DE ESCANEO
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .background(
                    Color(0xFF344550),
                    RoundedCornerShape(14.dp)
                )
        ) {

            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(
                    onClick = { luzActiva = !luzActiva },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (luzActiva)
                            Color(0xFFFFE6A3)
                        else
                            Color.White
                    )
                ) {
                    Text(
                        text = if (luzActiva) "Luz ON" else "Luz",
                        color = Color.Black
                    )
                }

                Button(
                    onClick = { modoQR = !modoQR },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Text(
                        text = if (modoQR)
                            "DataMatrix / QR"
                        else
                            "1D / GS1",
                        color = AzulEscaner,
                        fontSize = 12.sp
                    )
                }
            }

            // RECUADRO DEL ESCÁNER
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(210.dp)
                        .border(
                            3.dp,
                            Color(0xFF38B64B),
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "Área de escaneo",
                        tint = Color(0xFF71E48A),
                        modifier = Modifier.size(65.dp)
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "ALINEAR CÓDIGO",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "● Sensor Activo",
                    color = Color(0xFF81E68B),
                    fontSize = 12.sp
                )

                Text(
                    "ISO 15415 / 15416",
                    color = Color.White,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // PRODUCTO DETECTADO
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(Modifier.padding(16.dp)) {

                Text(
                    text = "✓ ¡CÓDIGO DETECTADO!",
                    color = Color(0xFF13752A),
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = codigoDetectado,
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "PF   FEFO PRIORITARIO",
                    color = Color(0xFF16802E),
                    fontSize = 12.sp
                )

                Text(
                    text = "PARACETAMOL 500 mg Tableta",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Caja hospitalaria x 100 tabletas blísterizadas",
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(18.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    DatoEscaneo(
                        titulo = "LOTE DETECTADO",
                        valor = "PAR-2403",
                        modifier = Modifier.weight(1f)
                    )

                    DatoEscaneo(
                        titulo = "VENCIMIENTO",
                        valor = "10 / 2026",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(10.dp))

                DatoEscaneo(
                    titulo = "UBICACIÓN SUGERIDA",
                    valor = "Anaquel C-01-N1    Zona A",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Avance de lote en sesión",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = "$bultos de 12 bultos (${bultos * 100 / 12}%)",
                    fontSize = 12.sp
                )

                Spacer(Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { bultos / 12f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp),
                    color = Color(0xFF147A34)
                )

                Spacer(Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (bultos < 12) bultos++
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulEscaner
                    )
                ) {
                    Text("Continuar a Recepción / Conteo →")
                }

                Button(
                    onClick = onConsultarStock,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDCE8F7)
                    )
                ) {
                    Text(
                        text = "Consultar Ficha de Stock",
                        color = Color(0xFF102C3B)
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // BÚSQUEDA MANUAL
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFEAF1FC)
            )
        ) {

            Column(Modifier.padding(14.dp)) {

                Text(
                    text = "⚠ ¿Etiqueta dañada o ilegible?",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Ingrese el código alfanumérico visible o el número de lote para recuperación forzada de catálogo.",
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    text = "Código GTIN, Barra o Lote",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(6.dp))

                OutlinedTextField(
                    value = codigoManual,
                    onValueChange = { codigoManual = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Ej. 7750123984120")
                    },
                    singleLine = true
                )

                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {
                        if (codigoManual.isNotBlank()) {
                            codigoDetectado = codigoManual
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulEscaner
                    )
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Buscar")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // TERMINAL ZEBRA
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE0EDFF)
            )
        ) {

            Row(
                modifier = Modifier.padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = null,
                    tint = AzulEscaner
                )

                Spacer(Modifier.width(12.dp))

                Column {

                    Text(
                        text = "Compatibilidad con Terminal Zebra",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Text(
                        text = "El disparador físico lateral activa el haz óptico de alta precisión sin tocar pantalla.",
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(25.dp))
    }
}


// COMPONENTE COMÚN DE DATOS DEL ESCÁNER
@Composable
private fun DatoEscaneo(
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .background(
                Color(0xFFEAF1FC),
                RoundedCornerShape(7.dp)
            )
            .padding(10.dp)
    ) {

        Text(
            text = titulo,
            fontSize = 10.sp,
            color = Color(0xFF56636C)
        )

        Spacer(Modifier.height(5.dp))

        Text(
            text = valor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}