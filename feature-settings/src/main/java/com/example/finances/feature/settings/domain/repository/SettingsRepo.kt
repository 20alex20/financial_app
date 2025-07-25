package com.example.finances.feature.settings.domain.repository

import com.example.finances.core.ui.theme.ThemeController
import com.example.finances.feature.settings.data.enums.VibrationDuration
import com.example.finances.feature.settings.data.enums.PrimaryColor
import com.example.finances.feature.settings.data.enums.ThemeMode

interface SettingsRepo: ExternalSettingsRepo {
    override val themeController: ThemeController

    fun saveTheme(mode: ThemeMode): Boolean

    fun loadTheme(): ThemeMode

    fun savePrimaryColor(primaryColor: PrimaryColor): Boolean

    fun loadPrimaryColor(): PrimaryColor

    fun saveVibrationDuration(vibrationDuration: VibrationDuration): Boolean

    fun loadVibrationDuration(): VibrationDuration

    fun saveUserPin(userPin: String?): Boolean

    override fun loadUserPin(): String?
}
