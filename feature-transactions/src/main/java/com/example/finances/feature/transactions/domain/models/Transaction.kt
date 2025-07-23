package com.example.finances.feature.transactions.domain.models

import java.time.LocalDateTime

data class Transaction(
    val id: Int,
    val categoryId: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val dateTime: LocalDateTime,
    val amount: Double,
    val comment: String
)
