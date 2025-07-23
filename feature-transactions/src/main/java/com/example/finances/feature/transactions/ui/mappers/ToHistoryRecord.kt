package com.example.finances.feature.transactions.ui.mappers

import com.example.finances.core.managers.ConvertAmountUseCase
import com.example.finances.core.utils.models.Currency
import com.example.finances.feature.transactions.domain.DateTimeFormatters
import com.example.finances.feature.transactions.domain.models.Transaction
import com.example.finances.feature.transactions.ui.models.HistoryRecord
import java.time.LocalDate

fun Transaction.toHistoryRecord(
    currency: Currency,
    today: LocalDate,
    convertAmountUseCase: ConvertAmountUseCase
) = HistoryRecord(
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
