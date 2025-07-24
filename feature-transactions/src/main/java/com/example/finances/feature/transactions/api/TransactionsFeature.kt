package com.example.finances.feature.transactions.api

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.example.finances.core.Feature
import com.example.finances.core.navigation.NavBarRoutes
import com.example.finances.core.utils.viewmodel.ViewModelMapProvider
import com.example.finances.feature.account.di.AccountViewModelMapProvider
import com.example.finances.feature.account.domain.repository.ExternalTransactionsRepo
import com.example.finances.feature.transactions.navigation.TransactionsNavRoutes
import com.example.finances.feature.transactions.navigation.transactionsNavigation
import javax.inject.Inject

class TransactionsFeature @Inject constructor(
    private val accountViewModelMapProvider: AccountViewModelMapProvider,
    private val externalTransactionsRepo: ExternalTransactionsRepo
) : Feature {
    override fun getViewModelMapProvider(): ViewModelMapProvider = accountViewModelMapProvider

    fun getExternalTransactionsRepo() = externalTransactionsRepo

    override fun registerGraph(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        navGraphBuilder.apply {
            navigation<NavBarRoutes.Expenses>(
                startDestination = TransactionsNavRoutes.ExpensesIncome(isIncome = false)
            ) {
                transactionsNavigation(this, navController)
            }
        }
        navGraphBuilder.apply {
            navigation<NavBarRoutes.Income>(
                startDestination = TransactionsNavRoutes.ExpensesIncome(isIncome = true)
            ) {
                transactionsNavigation(this, navController)
            }
        }
    }
}
