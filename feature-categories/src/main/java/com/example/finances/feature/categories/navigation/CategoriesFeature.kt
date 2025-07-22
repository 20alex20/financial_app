package com.example.finances.feature.categories.navigation

import androidx.lifecycle.ViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.finances.core.navigation.FeatureNavigation
import com.example.finances.core.navigation.NavBarRoutes
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.core.utils.viewmodel.ViewModelMapProvider
import com.example.finances.feature.categories.ui.CategoriesScreen
import javax.inject.Inject
import javax.inject.Provider

class CategoriesFeature @Inject constructor(
    private val map: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<BaseViewModel>>
) : FeatureNavigation, ViewModelMapProvider {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.apply {
            composable<NavBarRoutes.Categories> {
                CategoriesScreen()
            }
        }
    }

    override fun provideViewModels(): Map<Class<out ViewModel>, Provider<BaseViewModel>> = map
}
