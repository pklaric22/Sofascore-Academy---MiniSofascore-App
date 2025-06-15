package com.example.minisofascoreapp

import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.example.minisofascoreapp.presentation.navigation.AppNavGraph
import com.example.minisofascoreapp.ui.theme.MiniSofascoreAppTheme

@Composable
fun MiniSofascoreApp() {
    var isDarkTheme by remember { mutableStateOf(false) }

    MiniSofascoreAppTheme(darkTheme = isDarkTheme) {
        val navController = rememberNavController()

        AppNavGraph(
            navController = navController,
            isDarkTheme = isDarkTheme,
            onToggleTheme = { isDarkTheme = !isDarkTheme }
        )
    }
}
