package com.example.finances.features.transactions.ui.mappers

import com.example.finances.core.utils.usecases.ConvertAmountUseCase
import com.example.finances.core.utils.models.Currency
import com.example.finances.features.transactions.domain.models.Transaction
import com.example.finances.features.transactions.ui.models.ExpenseIncome

private val convertAmountUseCase = ConvertAmountUseCase()

fun Transaction.toExpenseIncome(currency: Currency) = ExpenseIncome(
    id = id,
    categoryName = categoryName,
    categoryEmoji = categoryEmoji,
    amount = convertAmountUseCase(amount, currency),
    comment = comment
)
