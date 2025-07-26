package com.example.finances.feature.transactions.data.mappers

import com.example.finances.feature.transactions.data.models.TransactionEntity
import com.example.finances.feature.transactions.domain.models.ShortTransaction
import com.example.finances.feature.transactions.domain.models.Transaction

fun ShortTransaction.toTransaction(localId: Int) = Transaction(
    id = localId,
    categoryId = categoryId,
    categoryName = categoryName,
    categoryEmoji = categoryEmoji,
    dateTime = dateTime,
    amount = amount,
    comment = comment
)

fun TransactionEntity.toTransaction() = Transaction(
    id = id.toInt(),
    categoryId = categoryId,
    categoryName = categoryName,
    categoryEmoji = categoryEmoji,
    dateTime = dateTime,
    amount = amount,
    comment = comment
)
