package com.jnarvaez.aplicacionupc2749.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jnarvaez.aplicacionupc2749.presentation.screens.buscar.BuscarScreen
import com.jnarvaez.aplicacionupc2749.presentation.screens.detalle.DetalleScreen
import com.jnarvaez.aplicacionupc2749.presentation.screens.form.FormScreen
import com.jnarvaez.aplicacionupc2749.presentation.screens.inicio.InicioScreen

@Composable
fun AppNavigation(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Inicio.ruta,
        modifier = Modifier
    ){
        composable(BottomNavItem.Inicio.ruta){
            InicioScreen()
        }
        composable(BottomNavItem.Form.ruta){
            FormScreen()
        }
        composable(BottomNavItem.Buscar.ruta){
            BuscarScreen()
        }
        composable(BottomNavItem.Detalle.ruta){
            DetalleScreen()
        }
    }
}