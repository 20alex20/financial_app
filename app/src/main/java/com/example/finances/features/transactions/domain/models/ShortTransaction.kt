package com.example.finances.features.transactions.domain.models

import java.time.LocalDateTime

data class ShortTransaction(
    val categoryId: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val amount: Double,
    val dateTime: LocalDateTime,
    val comment: String
)
