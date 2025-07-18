package com.example.finances.features.transactions.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val remoteId: Int?,
    val categoryId: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val amount: Double,
    val dateTime: LocalDateTime,
    val comment: String,
    val isIncome: Boolean,
    val isSynced: Boolean
)
