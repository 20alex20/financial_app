package com.example.finances.app.navigation

import androidx.navigation.NavGraphBuilder
import com.example.finances.app.di.AccountNavigation
import com.example.finances.app.di.CategoriesNavigation
import com.example.finances.app.di.SettingsNavigation
import com.example.finances.app.di.TransactionsNavigation
import com.example.finances.core.navigation.FeatureNavigation
import javax.inject.Inject

class AppNavigationCoordinator @Inject constructor(
    @CategoriesNavigation private val categoriesNavigation: FeatureNavigation,
    @AccountNavigation private val accountNavigation: FeatureNavigation,
    @TransactionsNavigation private val transactionsNavigation: FeatureNavigation,
    @SettingsNavigation private val settingsNavigation: FeatureNavigation
) : FeatureNavigation {
    override fun registerGraph(navGraphBuilder: NavGraphBuilder) {
        transactionsNavigation.registerGraph(navGraphBuilder)
        accountNavigation.registerGraph(navGraphBuilder)
        categoriesNavigation.registerGraph(navGraphBuilder)
        settingsNavigation.registerGraph(navGraphBuilder)
    }
}
