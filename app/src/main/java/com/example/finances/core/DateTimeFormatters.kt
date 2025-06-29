package com.example.finances.core

import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

object DateTimeFormatters {
    val time: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val date: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val dateTime: DateTimeFormatter = DateTimeFormatterBuilder()
        .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
        .appendPattern("'Z'")
        .toFormatter()
}
