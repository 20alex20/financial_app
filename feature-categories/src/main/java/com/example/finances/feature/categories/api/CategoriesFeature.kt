package com.example.finances.feature.categories.api

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.finances.core.navigation.NavBarRoutes
import com.example.finances.core.Feature
import com.example.finances.core.utils.viewmodel.ViewModelMapProvider
import com.example.finances.feature.categories.di.CategoriesViewModelMapProvider
import com.example.finances.feature.categories.domain.repository.CategoriesRepo
import com.example.finances.feature.categories.ui.CategoriesScreen
import javax.inject.Inject

class CategoriesFeature @Inject constructor(
    private val categoriesViewModelMapProvider: CategoriesViewModelMapProvider,
    private val categoriesRepo: CategoriesRepo
) : Feature {
    override fun getViewModelMapProvider(): ViewModelMapProvider = categoriesViewModelMapProvider

    fun getCategoriesRepo() = categoriesRepo

    override fun registerGraph(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.apply {
            composable<NavBarRoutes.Categories> {
                CategoriesScreen()
            }
        }
    }
}
