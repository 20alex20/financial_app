package com.example.finances.features.history.domain.mappers

import com.example.finances.core.DateTimeFormatters
import com.example.finances.core.data.repository.common.TransactionResponse
import com.example.finances.features.history.domain.models.HistoryRecord
import java.time.LocalDateTime

fun TransactionResponse.toHistoryRecord() = HistoryRecord(
    id = id,
    categoryName = category.name,
    categoryEmoji = category.emoji,
    dateTime = LocalDateTime.parse(transactionDate, DateTimeFormatters.dateTime),
    amount = amount.toDouble(),
    comment = comment
)
