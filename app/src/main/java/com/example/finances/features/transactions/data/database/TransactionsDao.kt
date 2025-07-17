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

    @Query(
        "SELECT * FROM transactions " +
                "WHERE (:transactionId > 0 AND id = :transactionId) OR " +
                "(:transactionId < 0 AND localId = -:transactionId)"
    )
    suspend fun getTransaction(transactionId: Int): TransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Int

    @Query(
        "DELETE FROM transactions " +
                "WHERE (:transactionId > 0 AND id = :transactionId) OR " +
                "(:transactionId < 0 AND localId = -:transactionId)"
    )
    suspend fun deleteTransaction(transactionId: Int)
}
