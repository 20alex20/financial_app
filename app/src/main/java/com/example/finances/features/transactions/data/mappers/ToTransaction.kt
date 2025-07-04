package com.example.finances.features.transactions.data.mappers

import com.example.finances.core.domain.DateTimeFormatters
import com.example.finances.features.transactions.data.models.TransactionResponse
import com.example.finances.features.transactions.domain.models.Transaction
import java.time.LocalDateTime

fun TransactionResponse.toTransaction() = Transaction(
    id = id,
    categoryName = category.name,
    categoryEmoji = category.emoji,
    dateTime = LocalDateTime.parse(transactionDate, DateTimeFormatters.replyDateTime),
    amount = amount.toDouble(),
    comment = comment
)
