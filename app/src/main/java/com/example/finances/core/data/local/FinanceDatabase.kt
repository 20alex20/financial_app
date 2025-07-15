package com.example.finances.core.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.finances.core.data.local.converters.DateTimeConverters
import com.example.finances.core.data.local.dao.AccountDao
import com.example.finances.core.data.local.dao.CategoryDao
import com.example.finances.core.data.local.dao.TransactionDao
import com.example.finances.core.data.local.entities.AccountEntity
import com.example.finances.core.data.local.entities.CategoryEntity
import com.example.finances.core.data.local.entities.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        CategoryEntity::class,
        AccountEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTimeConverters::class)
abstract class FinanceDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao

    companion object {
        private const val DATABASE_NAME = "finance_db"

        @Volatile
        private var INSTANCE: FinanceDatabase? = null

        fun getInstance(context: Context): FinanceDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): FinanceDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                FinanceDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
} 