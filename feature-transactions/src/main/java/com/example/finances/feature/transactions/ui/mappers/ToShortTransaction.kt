package com.example.finances.feature.transactions.ui.mappers

import com.example.finances.feature.transactions.domain.models.ShortTransaction
import com.example.finances.feature.transactions.domain.models.Transaction

fun Transaction.toShortTransaction() = ShortTransaction(
    categoryId = categoryId,
    categoryName = categoryName,
    categoryEmoji = categoryEmoji,
    amount = amount,
    dateTime = dateTime,
    comment = comment
)
