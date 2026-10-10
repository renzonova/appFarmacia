package com.jnarvaez.aplicacionupc2749.data.remote

import com.jnarvaez.aplicacionupc2749.presentation.screens.inventario.Producto
import java.time.LocalDate

fun Producto.toDto(): ProductoDto {
    return ProductoDto(
        id = this.codigo,
        codigo = this.codigo,
        producto = this.producto,
        tipo = this.tipo,
        lote = this.lote,
        vencimiento = this.vencimiento,
        ubicacion = this.ubicacion,
        stock = this.stock,
        minimo = this.minimo,
        unidad = this.unidad,
        estado = this.estado
    )
}

fun ProductoDto.toDomain(): Producto {
    val parsedDate = try {
        val parts = this.vencimiento.split("/")
        if (parts.size == 3) {
            LocalDate.of(parts[2].toInt(), parts[1].toInt(), parts[0].toInt())
        } else {
            LocalDate.now()
        }
    } catch (e: Exception) {
        LocalDate.now()
    }

    return Producto(
        tipo = this.tipo,
        codigo = this.codigo ?: this.id,
        producto = this.producto,
        lote = this.lote,
        vencimiento = this.vencimiento,
        fechaVencimiento = parsedDate,
        ubicacion = this.ubicacion,
        minimo = this.minimo,
        stock = this.stock,
        unidad = this.unidad,
        estado = this.estado ?: "Stock Óptimo",
        estadoTipo = "normal",
        accion = "Kárdex"
    )
}
