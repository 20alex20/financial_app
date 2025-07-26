package com.example.finances.feature.settings.domain.repository

import androidx.compose.runtime.State
import com.example.finances.core.utils.models.ThemeParameters

interface ExternalSettingsRepo {
    fun getThemeParameters(): State<ThemeParameters>
    fun loadUserPin(): String?
}
