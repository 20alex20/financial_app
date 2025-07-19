package com.example.finances.features.transactions.data.mappers

import com.example.finances.features.transactions.data.models.ShortTransactionResponse
import com.example.finances.features.transactions.data.models.TransactionEntity
import com.example.finances.features.transactions.data.models.TransactionResponse
import com.example.finances.features.transactions.domain.DateTimeFormatters
import com.example.finances.features.transactions.domain.models.ShortTransaction
import java.time.LocalDateTime

fun TransactionResponse.toShortTransaction() = ShortTransaction(
    categoryName = category.name,
    categoryId = category.id,
    categoryEmoji = category.emoji,
    amount = amount.toDouble(),
    dateTime = LocalDateTime.parse(transactionDate, DateTimeFormatters.replyDateTime),
    comment = comment ?: ""
)

fun ShortTransactionResponse.toShortTransaction(
    categoryName: String,
    categoryEmoji: String
) = ShortTransaction(
    categoryId = categoryId,
    categoryName = categoryName,
    categoryEmoji = categoryEmoji,
    dateTime = LocalDateTime.parse(transactionDate, DateTimeFormatters.replyDateTime),
    amount = amount.toDouble(),
    comment = comment ?: ""
)

fun TransactionEntity.toShortTransaction() = ShortTransaction(
    categoryId = categoryId,
    categoryName = categoryName,
    categoryEmoji = categoryEmoji,
    dateTime = dateTime,
    amount = amount,
    comment = comment
)
