package com.example.finances.feature.settings.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finances.feature.settings.ui.AboutScreen
import com.example.finances.feature.settings.ui.PrimaryColorScreen
import com.example.finances.feature.settings.ui.SettingsScreen
import com.example.finances.feature.settings.ui.UserPinScreen
import com.example.finances.feature.settings.ui.VibrationScreen

@Composable
fun SettingsNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SettingsRoutes.Settings,
        modifier = modifier.fillMaxSize()
    ) {
        composable<SettingsRoutes.Settings> {
            SettingsScreen(navController)
        }
        composable<SettingsRoutes.PrimaryColor> {
            PrimaryColorScreen(navController)
        }
        composable<SettingsRoutes.Vibration> {
            VibrationScreen(navController)
        }
        composable<SettingsRoutes.UserPin> {
            UserPinScreen(navController)
        }
        composable<SettingsRoutes.About> {
            AboutScreen(navController)
        }
    }
}
