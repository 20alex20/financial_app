package com.example.finances.feature.categories.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.finances.core.navigation.FeatureNavigation
import com.example.finances.feature.categories.ui.CategoriesScreen
import javax.inject.Inject

class CategoriesNavigation @Inject constructor() : FeatureNavigation {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.apply {
            composable(CategoriesNavRoutes.Categories.route) {
                CategoriesScreen()
            }
        }
    }
} 