package com.example.finances.features.transactions.navigation

import com.example.finances.features.transactions.domain.ScreenType
import kotlinx.serialization.Serializable

/**
 * Запечатанный класс содержит объекты, соответствующие экранам транзакций
 */
sealed interface TransactionsNavRoutes {
    @Serializable data class ExpensesIncome(val screenType: ScreenType) : TransactionsNavRoutes
    @Serializable data class History(val screenType: ScreenType) : TransactionsNavRoutes
    @Serializable data class CreateUpdate(
        val screenType: ScreenType,
        val transactionId: Int? = null
    ) : TransactionsNavRoutes
}
