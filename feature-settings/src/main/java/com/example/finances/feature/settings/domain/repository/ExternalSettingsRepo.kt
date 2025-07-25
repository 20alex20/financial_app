package com.example.finances.feature.settings.domain.repository

import com.example.finances.core.ui.theme.ThemeController

interface ExternalSettingsRepo {
    val themeController: ThemeController

    fun loadUserPin(): String?
}
