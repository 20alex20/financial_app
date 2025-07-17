package com.example.finances.features.transactions.ui.mappers

import com.example.finances.features.transactions.domain.models.Transaction
import com.example.finances.features.transactions.domain.models.ShortTransaction

fun ShortTransaction.toTransaction(
    id: Int,
    categoryName: String,
    categoryEmoji: String
) = Transaction(
    id = id,
    categoryId = categoryId,
    categoryName = categoryName,
    categoryEmoji = categoryEmoji,
    dateTime = dateTime,
    amount = amount,
    comment = comment
)
