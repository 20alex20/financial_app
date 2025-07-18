package com.example.finances.features.transactions.ui.mappers

import com.example.finances.features.transactions.domain.models.Transaction
import com.example.finances.features.transactions.domain.models.ShortTransaction

fun Transaction.toShortTransaction() = ShortTransaction(
    categoryId = categoryId,
    categoryName = categoryName,
    categoryEmoji = categoryEmoji,
    amount = amount,
    dateTime = dateTime,
    comment = comment
)
