package com.example.finances.features.history.domain.mappers

import com.example.finances.core.DateTimeFormatters
import com.example.finances.core.data.repository.models.Currency
import com.example.finances.features.history.domain.models.HistoryRecord
import com.example.finances.features.history.ui.models.UiHistoryRecord
import java.time.LocalDate

fun HistoryRecord.toUiHistoryRecord(currency: Currency, today: LocalDate) = UiHistoryRecord(
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
