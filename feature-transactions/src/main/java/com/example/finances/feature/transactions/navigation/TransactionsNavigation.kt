package com.example.finances.feature.transactions.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.finances.core.navigation.FeatureNavigation
import com.example.finances.feature.transactions.ui.ExpensesIncomeScreen
import com.example.finances.feature.transactions.ui.HistoryScreen
import javax.inject.Inject

class TransactionsNavigation @Inject constructor() : FeatureNavigation {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.apply {
            composable(TransactionsNavRoutes.ExpensesIncome.route) {
                ExpensesIncomeScreen(
                    onNavigateToHistory = {
                        navController.navigate(TransactionsNavRoutes.History.route)
                    }
                )
            }
            
            composable(TransactionsNavRoutes.History.route) {
                HistoryScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
} 