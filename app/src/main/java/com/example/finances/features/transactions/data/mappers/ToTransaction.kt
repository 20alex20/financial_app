package com.example.finances.features.transactions.data.mappers

import com.example.finances.features.transactions.data.models.TransactionEntity
import com.example.finances.features.transactions.data.models.ShortTransactionResponse
import com.example.finances.features.transactions.domain.DateTimeFormatters
import com.example.finances.features.transactions.data.models.TransactionResponse
import com.example.finances.features.transactions.domain.models.Transaction
import java.time.LocalDateTime

fun TransactionResponse.toTransaction() = Transaction(
    id = id,
    categoryId = category.id,
    categoryName = category.name,
    categoryEmoji = category.emoji,
    dateTime = LocalDateTime.parse(transactionDate, DateTimeFormatters.replyDateTime),
    amount = amount.toDouble(),
    comment = comment ?: ""
)

fun ShortTransactionResponse.toTransaction(
    categoryName: String,
    categoryEmoji: String
) = Transaction(
    id = id,
    categoryId = categoryId,
    categoryName = categoryName,
    categoryEmoji = categoryEmoji,
    dateTime = LocalDateTime.parse(transactionDate, DateTimeFormatters.replyDateTime),
    amount = amount.toDouble(),
    comment = comment ?: ""
)

fun TransactionEntity.toTransaction() = Transaction(
    id = id ?: -localId.toInt(),
    categoryId = categoryId,
    categoryName = categoryName,
    categoryEmoji = categoryEmoji,
    dateTime = dateTime,
    amount = amount,
    comment = comment
)
