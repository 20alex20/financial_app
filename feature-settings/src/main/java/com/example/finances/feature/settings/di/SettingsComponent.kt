package com.example.finances.feature.settings.di

import com.example.finances.feature.settings.api.SettingsDependencies
import com.example.finances.feature.settings.api.SettingsFeature
import com.example.finances.feature.settings.di.modules.PreferencesModule
import com.example.finances.feature.settings.di.modules.SettingsModule
import dagger.BindsInstance
import dagger.Component

@SettingsScope
@Component(
    dependencies = [SettingsComponent::class],
    modules = [
        SettingsModule::class,
        PreferencesModule::class
    ]
)
interface SettingsComponent {
    fun settingsFeature(): SettingsFeature

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance appVersion: String,
            dependencies: SettingsDependencies
        ): SettingsComponent
    }

    companion object {
        fun create(dependencies: SettingsDependencies, appVersion: String): SettingsComponent {
            return DaggerSettingsComponent.factory().create(appVersion, dependencies)
        }
    }
}
