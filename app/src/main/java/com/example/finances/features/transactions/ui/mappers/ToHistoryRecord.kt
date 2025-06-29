package com.example.finances.features.transactions.ui.mappers

import com.example.finances.features.transactions.domain.DateTimeFormatters
import com.example.finances.core.data.repository.models.Currency
import com.example.finances.features.transactions.domain.models.Transaction
import com.example.finances.features.transactions.ui.models.HistoryRecord
import java.time.LocalDate

fun Transaction.toHistoryRecord(currency: Currency, today: LocalDate) = HistoryRecord(
    id = id,
    categoryName = categoryName,
    categoryEmoji = categoryEmoji,
    dateTime = if (dateTime.toLocalDate() == today)
        dateTime.format(DateTimeFormatters.time)
    else
        dateTime.format(DateTimeFormatters.date),
    amount = currency.getStrAmount(amount),
    comment = comment
)
