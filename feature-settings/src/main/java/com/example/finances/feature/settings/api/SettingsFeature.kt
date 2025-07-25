package com.example.finances.feature.settings.api

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.example.finances.core.Feature
import com.example.finances.core.utils.viewmodel.ViewModelMapProvider
import com.example.finances.feature.settings.di.SettingsViewModelMapProvider
import com.example.finances.feature.settings.domain.repository.ExternalSettingsRepo
import javax.inject.Inject

class SettingsFeature @Inject constructor(
    private val categoriesViewModelMapProvider: SettingsViewModelMapProvider,
    private val externalSettingsRepo: ExternalSettingsRepo
) : Feature {
    override fun getViewModelMapProvider(): ViewModelMapProvider = categoriesViewModelMapProvider

    fun getExternalSettingsRepo(): ExternalSettingsRepo = externalSettingsRepo

    override fun registerGraph(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        navGraphBuilder.apply {

        }
    }
}
