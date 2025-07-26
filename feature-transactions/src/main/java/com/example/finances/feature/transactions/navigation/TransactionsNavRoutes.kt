package com.example.finances.feature.transactions.navigation

import kotlinx.serialization.Serializable

/**
 * Запечатанный класс содержит объекты, соответствующие экранам транзакций
 */
sealed interface TransactionsNavRoutes {
    @Serializable data class ExpensesIncome(val isIncome: Boolean) : TransactionsNavRoutes
    @Serializable data class History(val isIncome: Boolean) : TransactionsNavRoutes
    @Serializable data class Analysis(val isIncome: Boolean) : TransactionsNavRoutes
    @Serializable data class CreateUpdate(
        val isIncome: Boolean,
        val transactionId: Int? = null
    ) : TransactionsNavRoutes
}
