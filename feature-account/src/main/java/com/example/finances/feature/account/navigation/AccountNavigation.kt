package com.example.finances.feature.account.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.finances.core.navigation.FeatureNavigation
import com.example.finances.feature.account.ui.AccountScreen
import com.example.finances.feature.account.ui.EditAccountScreen
import javax.inject.Inject

class AccountNavigation @Inject constructor() : FeatureNavigation {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.apply {
            composable(AccountNavRoutes.Account.route) {
                AccountScreen(
                    onNavigateToEditAccount = { accountId ->
                        navController.navigate(AccountNavRoutes.EditAccount.createRoute(accountId))
                    }
                )
            }
            
            composable(
                route = AccountNavRoutes.EditAccount.route,
                arguments = AccountNavRoutes.EditAccount.arguments
            ) {
                EditAccountScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
} 