package com.example.finances.features.transactions.domain.mappers

import com.example.finances.core.data.repository.mappers.toStrAmount
import com.example.finances.core.data.repository.models.Currency
import com.example.finances.features.transactions.domain.models.Transaction
import com.example.finances.features.transactions.ui.models.ExpenseIncome

fun Transaction.toExpenseIncome(currency: Currency) = ExpenseIncome(
    id = id,
    categoryName = categoryName,
    categoryEmoji = categoryEmoji,
    amount = amount.toStrAmount(currency),
    comment = comment
)
