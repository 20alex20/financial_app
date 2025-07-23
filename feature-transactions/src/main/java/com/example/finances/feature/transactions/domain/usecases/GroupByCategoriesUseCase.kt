package com.example.finances.feature.transactions.domain.usecases

import com.example.finances.core.managers.ConvertAmountUseCase
import com.example.finances.core.utils.models.Currency
import com.example.finances.feature.transactions.di.TransactionsScope
import com.example.finances.feature.transactions.domain.models.Transaction
import com.example.finances.feature.transactions.ui.models.AnalysisCategory
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * Юзкейс для загрузки валюты счета
 */
@TransactionsScope
class GroupByCategoriesUseCase @Inject constructor(
    private val convertAmountUseCase: ConvertAmountUseCase
) {
    operator fun invoke(
        transactions: List<Transaction>,
        total: Double,
        currency: Currency
    ): List<AnalysisCategory> {
        return transactions.groupBy { it.categoryId }.map { (_, categoryTransactions) ->
            val amount = categoryTransactions.sumOf { it.amount }
            val percent = amount * 100.0 / total
            AnalysisCategory(
                name = categoryTransactions.first().categoryName,
                emoji = categoryTransactions.first().categoryEmoji,
                percent = if (percent < 1.0) "<1 %" else "${percent.roundToInt()} %",
                amount = convertAmountUseCase(amount, currency)
            )
        }
    }
}
