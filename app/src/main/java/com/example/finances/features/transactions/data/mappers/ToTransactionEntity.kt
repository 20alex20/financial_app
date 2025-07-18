package com.example.finances.features.transactions.data.mappers

import com.example.finances.features.transactions.data.models.TransactionEntity
import com.example.finances.features.transactions.domain.models.ShortTransaction
import com.example.finances.features.transactions.domain.models.Transaction

fun Transaction.toTransactionEntity(localId: Int, isIncome: Boolean) = TransactionEntity(
    localId = localId.toLong(),
    id = id,
    categoryId = categoryId,
    categoryName = categoryName,
    categoryEmoji = categoryEmoji,
    amount = amount,
    dateTime = dateTime,
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
    localId = localId.toLong(),
    id = transactionId,
    categoryId = categoryId,
    categoryName = categoryName,
    categoryEmoji = categoryEmoji,
    amount = amount,
    dateTime = dateTime,
    comment = comment,
    isIncome = isIncome
)
