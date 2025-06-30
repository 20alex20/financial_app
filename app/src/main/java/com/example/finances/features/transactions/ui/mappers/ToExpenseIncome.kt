package com.example.finances.features.transactions.ui.mappers

import com.example.finances.core.data.repository.models.Currency
import com.example.finances.features.transactions.domain.models.Transaction
import com.example.finances.features.transactions.ui.models.ExpenseIncome

fun Transaction.toExpenseIncome(currency: Currency) = ExpenseIncome(
    id = this.id,
    categoryName = this.categoryName,
    categoryEmoji = this.categoryEmoji,
    amount = currency.getStrAmount(this.amount),
    comment = this.comment
)
