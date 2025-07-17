package com.example.finances.features.transactions.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    val id: Int?,
    val categoryId: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val amount: Double,
    val dateTime: String,
    val comment: String,
    val isIncome: Boolean
)
