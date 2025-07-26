package com.example.finances.feature.settings.api

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.finances.core.Feature
import com.example.finances.core.navigation.NavBarRoutes
import com.example.finances.feature.settings.di.SettingsViewModelMapProvider
import com.example.finances.feature.settings.domain.repository.ExternalSettingsRepo
import com.example.finances.feature.settings.navigation.SettingsNavigation
import javax.inject.Inject

class SettingsFeature @Inject constructor(
    private val settingsViewModelMapProvider: SettingsViewModelMapProvider,
    private val externalSettingsRepo: ExternalSettingsRepo
) : Feature {
    override fun getViewModelMapProvider() = settingsViewModelMapProvider

    fun getExternalSettingsRepo(): ExternalSettingsRepo = externalSettingsRepo

    override fun registerGraph(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.apply {
            composable<NavBarRoutes.Settings> {
                SettingsNavigation()
            }
        }
    }
}
