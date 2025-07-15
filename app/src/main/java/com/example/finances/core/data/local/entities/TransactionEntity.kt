package com.example.finances.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.finances.features.transactions.domain.DateTimeFormatters
import com.example.finances.features.transactions.domain.models.ShortTransaction
import com.example.finances.features.transactions.domain.models.Transaction
import java.time.LocalDateTime

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: Int?,
    val accountId: Int,
    val categoryId: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val amount: Double,
    val transactionDate: LocalDateTime,
    val comment: String,
    val isIncome: Boolean,
    val isSynced: Boolean = false,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    fun toTransaction() = Transaction(
        id = id ?: 0,
        categoryName = categoryName,
        categoryEmoji = categoryEmoji,
        dateTime = transactionDate,
        amount = amount,
        comment = comment
    )

    fun toShortTransaction() = ShortTransaction(
        id = id,
        categoryId = categoryId,
        dateTime = transactionDate,
        amount = amount,
        comment = comment
    )

    companion object {
        fun fromTransaction(
            transaction: Transaction,
            accountId: Int,
            categoryId: Int,
            isIncome: Boolean,
            isSynced: Boolean = false
        ) = TransactionEntity(
            id = transaction.id,
            accountId = accountId,
            categoryId = categoryId,
            categoryName = transaction.categoryName,
            categoryEmoji = transaction.categoryEmoji,
            amount = transaction.amount,
            transactionDate = transaction.dateTime,
            comment = transaction.comment,
            isIncome = isIncome,
            isSynced = isSynced
        )

        fun fromShortTransaction(
            shortTransaction: ShortTransaction,
            accountId: Int,
            categoryName: String,
            categoryEmoji: String,
            isIncome: Boolean,
            isSynced: Boolean = false
        ) = TransactionEntity(
            id = shortTransaction.id,
            accountId = accountId,
            categoryId = shortTransaction.categoryId,
            categoryName = categoryName,
            categoryEmoji = categoryEmoji,
            amount = shortTransaction.amount,
            transactionDate = shortTransaction.dateTime,
            comment = shortTransaction.comment,
            isIncome = isIncome,
            isSynced = isSynced
        )
    }
} 