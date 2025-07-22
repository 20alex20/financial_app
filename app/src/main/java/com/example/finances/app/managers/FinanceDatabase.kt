package com.example.finances.app.managers

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.finances.feature.categories.api.CategoriesDatabase
import com.example.finances.feature.categories.data.database.CategoriesDao
import com.example.finances.feature.categories.data.models.CategoryEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Database(
    entities = [
        //TransactionEntity::class,
        CategoryEntity::class,
        //AccountEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTimeConverters::class)
abstract class FinanceDatabase : RoomDatabase(), CategoriesDatabase {
    //abstract fun transactionDao(): TransactionsDao
    abstract override fun categoryDao(): CategoriesDao
    //abstract fun accountDao(): AccountDao
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
