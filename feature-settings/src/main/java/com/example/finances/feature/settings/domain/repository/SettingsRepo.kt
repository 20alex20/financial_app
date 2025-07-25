package com.example.finances.feature.settings.domain.repository

import com.example.finances.feature.settings.domain.models.VibrationDuration
import com.example.finances.feature.settings.domain.models.PrimaryColor
import com.example.finances.feature.settings.domain.models.ThemeMode

interface SettingsRepo {
    fun saveTheme(mode: ThemeMode): Boolean

    fun loadTheme(): ThemeMode

    fun savePrimaryColor(primaryColor: PrimaryColor): Boolean

    fun loadPrimaryColor(): PrimaryColor

    fun saveVibrationDuration(vibrationDuration: VibrationDuration): Boolean

    fun loadVibrationDuration(): VibrationDuration

    fun saveUserPin(userPin: String?): Boolean

    fun loadUserPin(): String?
}
