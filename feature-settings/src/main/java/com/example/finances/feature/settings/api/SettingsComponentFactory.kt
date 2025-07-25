package com.example.finances.feature.settings.api

import com.example.finances.feature.settings.di.SettingsComponent

object SettingsComponentFactory {
    fun create(dependencies: SettingsDependencies): SettingsFeature {
        return SettingsComponent.create(dependencies).settingsFeature()
    }
}
