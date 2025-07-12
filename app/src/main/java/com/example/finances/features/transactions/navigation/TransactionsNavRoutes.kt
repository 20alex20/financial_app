package com.example.finances.features.transactions.navigation

import kotlinx.serialization.Serializable

/**
 * Запечатанный класс содержит объекты, соответствующие экранам и используемые для навигации
 */
sealed interface TransactionsNavRoutes {
    @Serializable data object Expenses : TransactionsNavRoutes
    @Serializable data object ExpensesHistory : TransactionsNavRoutes
    @Serializable data object Income : TransactionsNavRoutes
    @Serializable data object IncomeHistory : TransactionsNavRoutes
    @Serializable data class ExpensesCreateUpdate(val transactionId: Int?) : TransactionsNavRoutes
    @Serializable data class IncomeCreateUpdate(val transactionId: Int?) : TransactionsNavRoutes
}
