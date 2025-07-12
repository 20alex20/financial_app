package com.example.finances.features.transactions.data.mappers

import com.example.finances.features.transactions.domain.DateTimeFormatters
import com.example.finances.features.transactions.data.models.ShortTransactionResponse
import com.example.finances.features.transactions.data.models.TransactionResponse
import com.example.finances.features.transactions.domain.models.ShortTransaction
import java.time.LocalDateTime

fun ShortTransactionResponse.toShortTransaction() = ShortTransaction(
    id = id,
    categoryId = categoryId,
    dateTime = LocalDateTime.parse(transactionDate, DateTimeFormatters.replyDateTime),
    amount = amount.toDouble(),
    comment = comment ?: ""
)

fun TransactionResponse.toShortTransaction() = ShortTransaction(
    id = id,
    categoryId = category.id,
    dateTime = LocalDateTime.parse(transactionDate, DateTimeFormatters.replyDateTime),
    amount = amount.toDouble(),
    comment = comment ?: ""
)
