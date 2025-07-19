package com.example.finances.core.managers

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.finances.features.account.data.database.AccountDao
import com.example.finances.features.categories.data.database.CategoriesDao
import com.example.finances.features.transactions.data.database.TransactionsDao
import com.example.finances.features.account.data.models.AccountEntity
import com.example.finances.features.categories.data.models.CategoryEntity
import com.example.finances.features.transactions.data.models.TransactionEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
    abstract fun transactionDao(): TransactionsDao
    abstract fun categoryDao(): CategoriesDao
    abstract fun accountDao(): AccountDao
}

class DateTimeConverters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.format(formatter)
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }
}
