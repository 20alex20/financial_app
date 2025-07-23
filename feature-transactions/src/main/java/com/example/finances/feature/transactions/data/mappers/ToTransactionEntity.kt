package com.example.finances.feature.transactions.data.mappers

import com.example.finances.feature.transactions.data.models.TransactionEntity
import com.example.finances.feature.transactions.domain.models.ShortTransaction

fun ShortTransaction.toTransactionEntity(
    id: Long,
    remoteId: Int?,
    isIncome: Boolean,
    isSynced: Boolean
) = TransactionEntity(
    id = id,
    remoteId = remoteId,
    categoryId = categoryId,
    categoryName = categoryName,
    categoryEmoji = categoryEmoji,
    amount = amount,
    dateTime = dateTime,
    comment = comment,
    isIncome = isIncome,
    isSynced = isSynced
)
