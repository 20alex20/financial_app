package com.example.finances.features.transactions.domain

import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

/**
 * Все форматы даты/времени, используемые в приложении
 */
object DateTimeFormatters {
    private const val MIN_DIGITS_AFTER_POINT = 0
    private const val MAX_DIGITS_AFTER_POINT = 9
    private const val REQUEST_DIGITS_AFTER_POINT = 3

    val time: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val date: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val requestDate: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val replyDateTime: DateTimeFormatter = DateTimeFormatterBuilder()
        .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        .appendFraction(
            ChronoField.NANO_OF_SECOND,
            MIN_DIGITS_AFTER_POINT,
            MAX_DIGITS_AFTER_POINT,
            true
        )
        .appendPattern("'Z'")
        .toFormatter()
    val requestDateTime: DateTimeFormatter = DateTimeFormatterBuilder()
        .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        .appendFraction(
            ChronoField.NANO_OF_SECOND,
            REQUEST_DIGITS_AFTER_POINT,
            REQUEST_DIGITS_AFTER_POINT,
            true
        )
        .appendPattern("'Z'")
        .toFormatter()
}
