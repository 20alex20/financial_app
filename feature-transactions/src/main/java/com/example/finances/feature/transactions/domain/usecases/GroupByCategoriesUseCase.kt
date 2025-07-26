package com.example.finances.feature.transactions.domain.usecases

import com.example.finances.feature.transactions.di.TransactionsScope
import com.example.finances.feature.transactions.domain.models.Transaction
import com.example.finances.feature.transactions.domain.models.AnalysisGroup
import javax.inject.Inject

@TransactionsScope
class GroupByCategoriesUseCase @Inject constructor() {
    operator fun invoke(
        transactions: List<Transaction>,
        total: Double
    ): List<AnalysisGroup> {
        return transactions.groupBy { it.categoryId }.map { (_, categoryTransactions) ->
            val amount = categoryTransactions.sumOf { it.amount }
            AnalysisGroup(
                name = categoryTransactions.first().categoryName,
                emoji = categoryTransactions.first().categoryEmoji,
                percent = amount * 100.0 / total,
                amount = amount
            )
        }
    }
}
