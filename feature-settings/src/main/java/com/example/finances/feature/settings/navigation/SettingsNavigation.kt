package com.example.finances.feature.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.finances.feature.settings.ui.AboutScreen
import com.example.finances.feature.settings.ui.PrimaryColorScreen
import com.example.finances.feature.settings.ui.SettingsScreen
import com.example.finances.feature.settings.ui.VibrationScreen

fun settingsNavigation(
    navGraphBuilder: NavGraphBuilder,
    navController: NavHostController
) = navGraphBuilder.apply {
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
        VibrationScreen(navController)
    }
    composable<SettingsRoutes.About> {
        AboutScreen(navController)
    }
}
