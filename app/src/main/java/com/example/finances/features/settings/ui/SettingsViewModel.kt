package com.example.finances.features.settings.ui

import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.features.settings.domain.models.Settings
import javax.inject.Inject

class SettingsViewModel @Inject constructor() : BaseViewModel() {
    fun getSettings(): Settings = Settings(
        isDarkTheme = false,
        isNotificationsEnabled = true
    )

    fun updateSettings(settings: Settings) {
        // TODO: Implement settings update
    }
}
