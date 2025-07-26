package com.example.finances.app.di.modules

import com.example.finances.BuildConfig
import com.example.finances.app.di.ActivityComponent
import com.example.finances.app.di.ActivityScope
import com.example.finances.app.di.SettingsNavigation
import com.example.finances.core.navigation.FeatureNavigation
import com.example.finances.core.utils.viewmodel.ViewModelMapProvider
import com.example.finances.feature.settings.api.SettingsComponentFactory
import com.example.finances.feature.settings.api.SettingsDependencies
import com.example.finances.feature.settings.api.SettingsFeature
import com.example.finances.feature.settings.domain.repository.ExternalSettingsRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
interface SettingsModule {
    @Binds
    fun bindsSettingsDependencies(dependencies: ActivityComponent): SettingsDependencies

    @Binds
    @SettingsNavigation
    fun bindsSettingsNavigation(settingsFeature: SettingsFeature): FeatureNavigation

    companion object {
        @Provides
        @ActivityScope
        fun providesSettingsFeature(dependencies: SettingsDependencies): SettingsFeature {
            return SettingsComponentFactory.create(dependencies, BuildConfig.VERSION_NAME)
        }

        @Provides
        @IntoSet
        fun providesSettingsViewModelProviders(
            settingsFeature: SettingsFeature
        ): ViewModelMapProvider {
            return settingsFeature.getViewModelMapProvider()
        }

        @Provides
        fun providesExternalSettingsRepo(settingsFeature: SettingsFeature): ExternalSettingsRepo {
            return settingsFeature.getExternalSettingsRepo()
        }
    }
}
