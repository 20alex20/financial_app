package com.example.finances.core.data.local.dao

import androidx.room.*
import com.example.finances.core.data.local.entities.TransactionEntity
import java.time.LocalDate

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE accountId = :accountId AND transactionDate BETWEEN :startDate AND :endDate AND isIncome = :isIncome ORDER BY transactionDate DESC")
    suspend fun getTransactions(accountId: Int, startDate: String, endDate: String, isIncome: Boolean): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE id = :transactionId")
    suspend fun getTransaction(transactionId: Int): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE isSynced = 0")
    suspend fun getUnsyncedTransactions(): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("UPDATE transactions SET isSynced = 1 WHERE id = :transactionId")
    suspend fun markTransactionSynced(transactionId: Int)
} 