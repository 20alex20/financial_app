package com.example.finances.features.transactions.data.mappers

import com.example.finances.features.transactions.data.models.TransactionEntity
import com.example.finances.features.transactions.domain.DateTimeFormatters
import com.example.finances.features.transactions.domain.models.ShortTransaction
import com.example.finances.features.transactions.domain.models.Transaction

fun Transaction.toTransactionEntity(isIncome: Boolean) = TransactionEntity(
    id = id,
    categoryId = categoryId,
    categoryName = categoryName,
    categoryEmoji = categoryEmoji,
    amount = amount,
    dateTime = dateTime.format(DateTimeFormatters.requestDateTime),
    comment = comment,
    isIncome = isIncome
)

fun ShortTransaction.toTransactionEntity(
    localId: Int,
    transactionId: Int?,
    categoryName: String,
    categoryEmoji: String,
    isIncome: Boolean
) = TransactionEntity(
    localId = localId,
    id = transactionId,
    categoryId = categoryId,
    categoryName = categoryName,
    categoryEmoji = categoryEmoji,
    amount = amount,
    dateTime = dateTime.format(DateTimeFormatters.requestDateTime),
    comment = comment,
    isIncome = isIncome
)
