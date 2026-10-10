package com.jnarvaez.aplicacionupc2749.presentation.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun AppScaffold(navController: NavHostController, content: @Composable (PaddingValues)->Unit ){
    Scaffold(
        topBar = { AppTopBar(navController) },
        bottomBar = { AppBottomBar(navController) }
    ) {paddingValues ->
        content(paddingValues)
    }
}