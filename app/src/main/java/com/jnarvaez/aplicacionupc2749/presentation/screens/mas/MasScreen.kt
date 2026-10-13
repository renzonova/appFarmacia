package com.jnarvaez.aplicacionupc2749.presentation.screens.mas

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AzulPrincipal = Color(0xFF07566A)
private val FondoPantalla = Color(0xFFF5F8FC)

@Composable
fun MasScreen(
    onVolver: () -> Unit = {},
    onCerrarSesion: () -> Unit = {}
) {
    var mostrarDialogoLogout by remember { mutableStateOf(false) }

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.White.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "INFORMACIÓN DEL SISTEMA",
                        color = Color(0xFFA0D2DB),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "Servicio de Farmacia",
                        color = Color.White,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                TextButton(onClick = onVolver) {
                    Text("‹ Volver", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // CONTENIDO
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // TARJETA DE APLICACIÓN Y VERSIÓN
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(AzulPrincipal, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MedicalServices,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Servicio de Farmacia",
                                color = Color(0xFF0F172A),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Gestión de Inventario & Almacén",
                                color = Color(0xFF64748B),
                                fontSize = 12.sp
                            )
                            Row(
                                modifier = Modifier.padding(top = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFE0F2FE), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "v2.4.0 • Build 2749",
                                        color = AzulPrincipal,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Sistema móvil para control de existencias hospitalarias, trazabilidad de lotes, control FEFO de caducidad, registro de movimientos y alerta de cadena de frío.",
                        color = Color(0xFF334155),
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }

            // TARJETA INSTITUCIONAL
            SeccionInfo(
                titulo = "Información Académica",
                icono = Icons.Default.School
            ) {
                ItemDato("Institución", "Universidad Peruana de Ciencias Aplicadas (UPC)")
                ItemDato("Proyecto", "aplicacionUPC2749 - Farmacia Hospitalaria")
                ItemDato("Ciclo", "Octubre 2026")
            }

            // TARJETA DE MÓDULOS DEL SISTEMA
            SeccionInfo(
                titulo = "Módulos y Capacidades",
                icono = Icons.Default.Verified
            ) {
                ItemModulo("📦 Inventario Central", "Control de existencias de Fármacos (PF), Dispositivos (DM) y Sanitarios (PS).")
                ItemModulo("🔄 Historial de Movimientos", "Auditoría de altas, bajas, entradas, salidas y actualizaciones.")
                ItemModulo("⏳ Algoritmo FEFO", "Priorización automática de dispensación por vencimiento más próximo.")
                ItemModulo("❄️ Cadena de Frío", "Monitoreo continuo de biológicos y vacunas (2.0°C a 8.0°C).")
                ItemModulo("📷 Escaneo Móvil", "Lectura de códigos de barras y QR para recepción rápida.")
            }

            // TARJETA DE ENTORNO Y CLOUD
            SeccionInfo(
                titulo = "Infraestructura & Cloud",
                icono = Icons.Default.CloudDone
            ) {
                ItemDato("Backend", "AWS API Gateway + AWS Lambda")
                ItemDato("Base de Datos", "Amazon DynamoDB (NoSQL)")
                ItemDato("Terminal", "TERM-FARM-01 (En línea)")
                ItemDato("Almacén", "Farmacia Central (Sede Principal - Nivel 1)")
            }

            // TARJETA DE USUARIO Y SESIÓN
            SeccionInfo(
                titulo = "Sesión Activa",
                icono = Icons.Default.Security
            ) {
                ItemDato("Usuario", "QF Supervisor")
                ItemDato("Identificador", "QF-9482")
                ItemDato("Rol", "Administrador de Farmacia")

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = { mostrarDialogoLogout = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFBA1A1A)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBA1A1A))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = Color(0xFFBA1A1A),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cerrar Sesión",
                        color = Color(0xFFBA1A1A),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // DIÁLOGO DE CIERRE DE SESIÓN
        if (mostrarDialogoLogout) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoLogout = false },
                title = { Text("¿Cerrar sesión?") },
                text = { Text("¿Deseas salir de la aplicación? Tendrás que iniciar sesión nuevamente con tus credenciales.") },
                confirmButton = {
                    Button(
                        onClick = {
                            mostrarDialogoLogout = false
                            onCerrarSesion()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBA1A1A))
                    ) {
                        Text("Cerrar Sesión", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoLogout = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
private fun SeccionInfo(
    titulo: String,
    icono: ImageVector,
    contenido: @Composable () -> Unit
) {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = AzulPrincipal,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = titulo,
                    color = AzulPrincipal,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            contenido()
        }
    }
}

@Composable
private fun ItemDato(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = etiqueta,
            color = Color(0xFF64748B),
            fontSize = 11.5.sp
        )
        Text(
            text = valor,
            color = Color(0xFF1E293B),
            fontSize = 11.5.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ItemModulo(titulo: String, descripcion: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = titulo,
            color = Color(0xFF1E293B),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = descripcion,
            color = Color(0xFF64748B),
            fontSize = 10.5.sp,
            lineHeight = 14.sp
        )
    }
}
