package com.example.finances.feature.account.api

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.finances.core.navigation.NavBarRoutes
import com.example.finances.core.Feature
import com.example.finances.core.utils.viewmodel.ViewModelMapProvider
import com.example.finances.feature.account.di.AccountViewModelMapProvider
import com.example.finances.feature.account.navigation.AccountNavRoutes
import com.example.finances.feature.account.ui.AccountScreen
import com.example.finances.feature.account.ui.EditAccountScreen
import javax.inject.Inject

class AccountFeature @Inject constructor(
    private val accountViewModelMapProvider: AccountViewModelMapProvider
) : Feature {
    override fun getViewModelMapProvider(): ViewModelMapProvider = accountViewModelMapProvider

    override fun registerGraph(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        navGraphBuilder.apply {
            navigation<NavBarRoutes.Account>(startDestination = AccountNavRoutes.Account) {
                composable<AccountNavRoutes.Account> {
                    AccountScreen(navController)
                }
                composable<AccountNavRoutes.EditAccount> {
                    EditAccountScreen(navController)
                }
            }
        }
    }
}
