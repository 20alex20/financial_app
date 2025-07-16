package com.example.finances.core.data.local.converters

import androidx.room.TypeConverter
import com.example.finances.features.transactions.domain.DateTimeFormatters
import java.time.LocalDateTime

class DateTimeConverters {
    @TypeConverter
    fun fromString(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, DateTimeFormatters.replyDateTime) }
    }

    @TypeConverter
    fun dateTimeToString(date: LocalDateTime?): String? {
        return date?.format(DateTimeFormatters.replyDateTime)
    }
} 