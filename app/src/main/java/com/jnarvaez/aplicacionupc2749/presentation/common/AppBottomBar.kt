package com.jnarvaez.aplicacionupc2749.presentation.common

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jnarvaez.aplicacionupc2749.presentation.navigation.BottomNavItem

@Composable
fun AppBottomBar(navController: NavHostController){
    val opciones = listOf(BottomNavItem.Inicio, BottomNavItem.Form, BottomNavItem.Buscar)

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val rutaActual = navBackStackEntry?.destination?.route

        opciones.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icono, "") },
                label = { Text(item.titulo) },
                selected = rutaActual?.startsWith(item.ruta.split("?")[0]) == true,
                onClick = {
                    navController.navigate(item.ruta){
                        popUpTo(navController.graph.findStartDestination().id){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}