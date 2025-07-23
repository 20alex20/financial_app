package com.example.finances.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.example.finances.core.navigation.FeatureNavigation
import com.example.finances.feature.account.api.AccountFeature
import com.example.finances.feature.categories.api.CategoriesFeature
import javax.inject.Inject

class AppNavigationCoordinator @Inject constructor(
    private val accountFeature: AccountFeature,
    private val categoriesFeature: CategoriesFeature,
    //private val transactionsNavigation: TransactionsNavigation,
    //private val settingsNavigation: SettingsNavigation
) : FeatureNavigation {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        //transactionsNavigation.registerGraph(navGraphBuilder, navController)
        //transactionsNavigation.registerGraph(navGraphBuilder, navController)
        accountFeature.registerGraph(navGraphBuilder, navController)
        categoriesFeature.registerGraph(navGraphBuilder, navController)
        //settingsNavigation.registerGraph(navGraphBuilder, navController)
    }
}
