package com.example.finances.app.di.modules

import com.example.finances.app.di.ActivityComponent
import com.example.finances.app.di.ActivityScope
import com.example.finances.app.di.CategoriesNavigation
import com.example.finances.app.di.SettingsNavigation
import com.example.finances.app.managers.FinanceDatabase
import com.example.finances.core.navigation.FeatureNavigation
import com.example.finances.core.ui.theme.ThemeController
import com.example.finances.core.utils.viewmodel.ViewModelMapProvider
import com.example.finances.feature.categories.api.CategoriesComponentFactory
import com.example.finances.feature.categories.api.CategoriesDatabase
import com.example.finances.feature.categories.api.CategoriesDependencies
import com.example.finances.feature.categories.api.CategoriesFeature
import com.example.finances.feature.categories.domain.repository.CategoriesRepo
import com.example.finances.feature.settings.api.SettingsComponentFactory
import com.example.finances.feature.settings.api.SettingsDependencies
import com.example.finances.feature.settings.api.SettingsFeature
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
            return SettingsComponentFactory.create(dependencies)
        }

        @Provides
        @IntoSet
        fun providesSettingsViewModelProviders(
            settingsFeature: SettingsFeature
        ): ViewModelMapProvider {
            return settingsFeature.getViewModelMapProvider()
        }

        @Provides
        fun providesThemeController(settingsFeature: SettingsFeature): ThemeController {
            return settingsFeature.getExternalSettingsRepo().themeController
        }
    }
}
