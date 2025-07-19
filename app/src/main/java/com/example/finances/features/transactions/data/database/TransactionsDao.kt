package com.example.finances.features.transactions.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.finances.features.transactions.data.models.TransactionEntity
import java.time.LocalDateTime

@Dao
interface TransactionsDao {
    @Query(
        "SELECT * FROM transactions " +
                "WHERE (dateTime BETWEEN :startDate AND :endDate) AND isIncome = :isIncome " +
                "ORDER BY dateTime DESC"
    )
    suspend fun getTransactions(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        isIncome: Boolean
    ): List<TransactionEntity>


    @Query("SELECT * FROM transactions WHERE isSynced = 0 ORDER BY dateTime DESC")
    suspend fun getNotSyncedTransactions(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransaction(id: Long): TransactionEntity?

    @Query("SELECT remoteId FROM transactions WHERE id = :id")
    suspend fun getTransactionRemoteId(id: Long): Int?

    @Query("SELECT id FROM transactions WHERE remoteId = :remoteId")
    suspend fun getTransactionId(remoteId: Int): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long
}
