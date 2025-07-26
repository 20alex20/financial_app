package com.example.finances.feature.transactions.api

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.finances.core.Feature
import com.example.finances.core.navigation.NavBarRoutes
import com.example.finances.core.utils.viewmodel.ViewModelMapProvider
import com.example.finances.feature.account.di.AccountViewModelMapProvider
import com.example.finances.feature.account.domain.repository.ExternalTransactionsRepo
import com.example.finances.feature.transactions.navigation.ScreenType
import com.example.finances.feature.transactions.navigation.TransactionsNavigation
import javax.inject.Inject

class TransactionsFeature @Inject constructor(
    private val accountViewModelMapProvider: AccountViewModelMapProvider,
    private val externalTransactionsRepo: ExternalTransactionsRepo
) : Feature {
    override fun getViewModelMapProvider(): ViewModelMapProvider = accountViewModelMapProvider

    fun getExternalTransactionsRepo() = externalTransactionsRepo

    override fun registerGraph(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.apply {
            composable<NavBarRoutes.Expenses> {
                TransactionsNavigation(ScreenType.Expenses)
            }
            composable<NavBarRoutes.Income> {
                TransactionsNavigation(ScreenType.Income)
            }
        }
    }
}
