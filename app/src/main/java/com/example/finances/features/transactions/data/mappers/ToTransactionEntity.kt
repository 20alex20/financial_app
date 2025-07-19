package com.example.finances.features.transactions.data.mappers

import com.example.finances.features.transactions.data.models.TransactionEntity
import com.example.finances.features.transactions.domain.models.ShortTransaction

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
