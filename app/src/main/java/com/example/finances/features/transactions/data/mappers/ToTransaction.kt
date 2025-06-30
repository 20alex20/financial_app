package com.example.finances.features.transactions.data.mappers

import com.example.finances.features.transactions.domain.DateTimeFormatters
import com.example.finances.features.transactions.data.models.TransactionResponse
import com.example.finances.features.transactions.domain.models.Transaction
import java.time.LocalDateTime

fun TransactionResponse.toTransaction() = Transaction(
    id = this.id,
    categoryName = this.category.name,
    categoryEmoji = this.category.emoji,
    dateTime = LocalDateTime.parse(transactionDate, DateTimeFormatters.replyDateTime),
    amount = this.amount.toDouble(),
    comment = this.comment
)
