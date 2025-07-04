package com.example.finances.features.transactions.ui.mappers

import com.example.finances.core.domain.ConvertAmountUseCase
import com.example.finances.core.domain.DateTimeFormatters
import com.example.finances.core.domain.models.Currency
import com.example.finances.features.transactions.domain.models.Transaction
import com.example.finances.features.transactions.ui.models.HistoryRecord
import java.time.LocalDate

private val convertAmountUseCase = ConvertAmountUseCase()

fun Transaction.toHistoryRecord(currency: Currency, today: LocalDate) = HistoryRecord(
    id = id,
    categoryName = categoryName,
    categoryEmoji = categoryEmoji,
    dateTime = when (dateTime.toLocalDate()) {
        today -> dateTime.format(DateTimeFormatters.time)
        else -> dateTime.format(DateTimeFormatters.date)
    },
    amount = convertAmountUseCase(amount, currency),
    comment = comment
)
