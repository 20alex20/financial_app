package com.example.finances.core.managers

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.finances.features.account.data.database.AccountDao
import com.example.finances.features.categories.data.database.CategoriesDao
import com.example.finances.features.transactions.data.database.TransactionsDao
import com.example.finances.features.account.data.models.AccountEntity
import com.example.finances.features.categories.data.models.CategoryEntity
import com.example.finances.features.transactions.data.models.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        CategoryEntity::class,
        AccountEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FinanceDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionsDao
    abstract fun categoryDao(): CategoriesDao
    abstract fun accountDao(): AccountDao
}
