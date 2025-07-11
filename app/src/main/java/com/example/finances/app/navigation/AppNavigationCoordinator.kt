package com.example.finances.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.example.finances.core.navigation.FeatureNavigation
import com.example.finances.feature.categories.navigation.CategoriesNavigation
import javax.inject.Inject

class AppNavigationCoordinator @Inject constructor(
    //private val accountNavigation: AccountNavigation,
    private val categoriesNavigation: CategoriesNavigation,
    //private val transactionsNavigation: TransactionsNavigation,
    //private val settingsNavigation: SettingsNavigation
) : FeatureNavigation {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        //transactionsNavigation.registerGraph(navGraphBuilder, navController)
        //transactionsNavigation.registerGraph(navGraphBuilder, navController)
        //accountNavigation.registerGraph(navGraphBuilder, navController)
        categoriesNavigation.registerGraph(navGraphBuilder, navController)
        //settingsNavigation.registerGraph(navGraphBuilder, navController)
    }
}
