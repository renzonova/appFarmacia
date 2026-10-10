package com.jnarvaez.aplicacionupc2749.presentation.navigation

object RutasNav {
    const val INICIO = "inicio"
    const val FORMULARIO = "form?id={id}"
    const val BUSCAR = "buscar"
    const val DETALLE = "detalle/{id}"

    fun getTitulo(ruta: String?): String{
        return when{
            ruta == INICIO -> "Catálogo Académico"
            ruta?.startsWith("form") == true -> "Formulario Libro"
            ruta == BUSCAR -> "Buscar Libro"
            ruta?.startsWith("detalle") == true -> "Detalle Libro"
            else -> ""
        }
    }
}