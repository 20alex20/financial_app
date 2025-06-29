package com.example.finances.features.transactions.domain.mappers

import com.example.finances.core.data.repository.common.TransactionResponse
import com.example.finances.features.transactions.domain.models.Transaction

fun TransactionResponse.toTransaction() = Transaction(
    id = this.id,
    categoryName = this.category.name,
    categoryEmoji = this.category.emoji,
    amount = this.amount.toDouble(),
    comment = this.comment
)
