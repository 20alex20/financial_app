package com.example.finances.features.transactions.ui.mappers

import com.example.finances.core.domain.ConvertAmountUseCase
import com.example.finances.core.domain.models.Currency
import com.example.finances.features.transactions.domain.models.Transaction
import com.example.finances.features.transactions.ui.models.ExpenseIncome

fun Transaction.toExpenseIncome(currency: Currency) = ExpenseIncome(
    id = id,
    amount = ConvertAmountUseCase.amountToString(amount, currency),
    category = category.name
)
