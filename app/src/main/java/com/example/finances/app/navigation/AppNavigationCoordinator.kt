package com.example.finances.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.example.finances.app.di.AccountNavigation
import com.example.finances.app.di.CategoriesNavigation
import com.example.finances.app.di.TransactionsNavigation
import com.example.finances.core.navigation.FeatureNavigation
import javax.inject.Inject

class AppNavigationCoordinator @Inject constructor(
    @CategoriesNavigation private val categoriesNavigation: FeatureNavigation,
    @AccountNavigation private val accountNavigation: FeatureNavigation,
    @TransactionsNavigation private val transactionsNavigation: FeatureNavigation,
    //private val settingsNavigation: SettingsNavigation
) : FeatureNavigation {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        transactionsNavigation.registerGraph(navGraphBuilder, navController)
        accountNavigation.registerGraph(navGraphBuilder, navController)
        categoriesNavigation.registerGraph(navGraphBuilder, navController)
        //settingsNavigation.registerGraph(navGraphBuilder, navController)
    }
}
