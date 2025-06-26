package com.example.minisofascoreapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.minisofascoreapp.presentation.eventdetails.EventDetailsScreen
import com.example.minisofascoreapp.presentation.eventlist.EventListScreen
import com.example.minisofascoreapp.presentation.tournamentDetails.TournamentDetailsScreen
import com.example.minisofascoreapp.presentation.SettingsScreen


@Composable
fun AppNavGraph(navController: NavHostController,
                isDarkTheme: Boolean,
                onToggleTheme: () -> Unit) {
    NavHost(
        navController = navController,
        startDestination = "event_list"
    ) {
        composable("event_list") {
            EventListScreen(
                onEventClick = { eventId ->
                    navController.navigate("eventDetails/$eventId")
                },
                onTournamentClick = { id ->
                    navController.navigate("tournamentDetails/$id")
                },
                onSettingsClick = {
                    navController.navigate("settings")
                }
            )
        }
        composable("eventDetails/{eventId}") { backStackEntry ->
            backStackEntry.arguments?.getString("eventId")?.toLongOrNull()
            EventDetailsScreen(
                onBackClick = { navController.popBackStack() },
                onTournamentClick = { id ->
                    navController.navigate("tournamentDetails/$id")
                })
        }
        composable("tournamentDetails/{tournamentId}") { backStackEntry ->
            backStackEntry.arguments?.getString("eventId")?.toLongOrNull()
            TournamentDetailsScreen(
                onBackClick = { navController.popBackStack() },
                onEventClick = { eventId ->
                    navController.navigate("eventDetails/$eventId")
                },
            )
        }
        composable("settings") {
            SettingsScreen(
                isDarkTheme = isDarkTheme,
                onThemeToggle = { onToggleTheme() },
                selectedDateFormat = "DD/MM/YYYY",
                onDateFormatChange = { /* TODO:  */ },
                selectedLanguage = "English",
                onLanguageChange = { /* TODO:  */ },
                onBackClick = { navController.popBackStack() }
            )
        }

        }
        }

