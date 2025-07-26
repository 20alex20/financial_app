package com.example.finances.feature.settings.domain.models

import com.example.finances.feature.settings.navigation.SettingsRoutes

data class SettingsItem(
    val name: String,
    val goTo: SettingsRoutes
)
