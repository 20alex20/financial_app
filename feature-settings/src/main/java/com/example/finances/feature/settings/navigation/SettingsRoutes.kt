package com.example.finances.feature.settings.navigation

import kotlinx.serialization.Serializable

sealed interface SettingsRoutes {
    @Serializable data object Settings : SettingsRoutes
    @Serializable data object PrimaryColor : SettingsRoutes
    @Serializable data object Vibration : SettingsRoutes
    @Serializable data object UserPin : SettingsRoutes
    @Serializable data object About : SettingsRoutes
}
