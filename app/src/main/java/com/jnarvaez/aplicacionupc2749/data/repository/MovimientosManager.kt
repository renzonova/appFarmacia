package com.jnarvaez.aplicacionupc2749.data.repository

import androidx.compose.runtime.mutableStateListOf
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

data class MovimientoHistorial(
    val id: String = UUID.randomUUID().toString().take(8),
    val fechaHora: String,
    val tipo: String, // "ACTUALIZACIÓN", "ELIMINACIÓN", "ENTRADA", "SALIDA", "AJUSTE"
    val codigoProducto: String,
    val nombreProducto: String,
    val lote: String,
    val cantidad: Int,
    val responsable: String,
    val descripcion: String
)

@Singleton
class MovimientosManager @Inject constructor() {

    val listaMovimientos = mutableStateListOf<MovimientoHistorial>()

    init {
        cargarHistorialInicial()
    }

    fun registrarMovimiento(
        tipo: String,
        codigoProducto: String,
        nombreProducto: String,
        lote: String,
        cantidad: Int,
        responsable: String = "QF Supervisor (QF-9482)",
        descripcion: String
    ) {
        val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val fechaActual = LocalDateTime.now().format(formato)

        val nuevo = MovimientoHistorial(
            fechaHora = fechaActual,
            tipo = tipo.uppercase().trim(),
            codigoProducto = codigoProducto,
            nombreProducto = nombreProducto,
            lote = lote,
            cantidad = cantidad,
            responsable = responsable,
            descripcion = descripcion
        )
        listaMovimientos.add(0, nuevo)
    }

    private fun cargarHistorialInicial() {
        val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val ahora = LocalDateTime.now()

        listaMovimientos.addAll(
            listOf(
                MovimientoHistorial(
                    fechaHora = ahora.minusHours(2).format(formato),
                    tipo = "ENTRADA",
                    codigoProducto = "PF-00482",
                    nombreProducto = "CEFTOXIZIMA 1 g Solución Inyectable",
                    lote = "CT-8902A",
                    cantidad = 100,
                    responsable = "QF Supervisor",
                    descripcion = "Ingreso por Orden de Compra OC-2026-88"
                ),
                MovimientoHistorial(
                    fechaHora = ahora.minusHours(5).format(formato),
                    tipo = "SALIDA",
                    codigoProducto = "PF-00109",
                    nombreProducto = "INSULINA NPH HUMANA 100 UI/ml",
                    lote = "IN-4022",
                    cantidad = -12,
                    responsable = "Lic. Farmacia Turno Mañana",
                    descripcion = "Dispensación a Unidad de Cuidados Intensivos (UCI)"
                ),
                MovimientoHistorial(
                    fechaHora = ahora.minusDays(1).format(formato),
                    tipo = "AJUSTE",
                    codigoProducto = "DM-00951",
                    nombreProducto = "CATÉTER INTRAVENOSO PERIFÉRICO N° 20G",
                    lote = "CT-2024-X",
                    cantidad = 5,
                    responsable = "Auditor Farmacéutico",
                    descripcion = "Ajuste por conteo físico en Gaveta G-12-C"
                )
            )
        )
    }
}
