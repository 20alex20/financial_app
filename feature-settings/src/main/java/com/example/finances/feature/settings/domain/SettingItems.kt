package com.example.finances.feature.settings.domain

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.finances.feature.settings.R
import com.example.finances.feature.settings.domain.models.SettingsItem
import com.example.finances.feature.settings.navigation.SettingsRoutes

@Composable
fun settingsItems() = listOf(
    SettingsItem(
        name = stringResource(R.string.primary_color),
        goTo = SettingsRoutes.PrimaryColor
    ),
    SettingsItem(
        name = stringResource(R.string.haptics),
        goTo = SettingsRoutes.Vibration
    ),
    SettingsItem(
        name = stringResource(R.string.code_password),
        goTo = SettingsRoutes.UserPin
    ),
    SettingsItem(
        name = stringResource(R.string.primary_color),
        goTo = SettingsRoutes.About
    )
)
