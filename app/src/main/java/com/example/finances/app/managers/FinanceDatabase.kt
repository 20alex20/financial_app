package com.example.finances.app.managers

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.finances.feature.account.api.AccountDatabase
import com.example.finances.feature.account.data.database.AccountDao
import com.example.finances.feature.account.data.models.AccountEntity
import com.example.finances.feature.categories.api.CategoriesDatabase
import com.example.finances.feature.categories.data.database.CategoriesDao
import com.example.finances.feature.categories.data.models.CategoryEntity
import com.example.finances.feature.transactions.api.TransactionsDatabase
import com.example.finances.feature.transactions.data.database.TransactionsDao
import com.example.finances.feature.transactions.data.models.TransactionEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Database(
    entities = [
        CategoryEntity::class,
        AccountEntity::class,
        TransactionEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTimeConverters::class)
abstract class FinanceDatabase
    : RoomDatabase(), CategoriesDatabase, AccountDatabase, TransactionsDatabase {
    abstract override fun categoryDao(): CategoriesDao
    abstract override fun accountDao(): AccountDao
    abstract override fun transactionsDao(): TransactionsDao
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
