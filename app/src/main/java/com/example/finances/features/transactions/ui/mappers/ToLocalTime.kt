package com.example.finances.features.transactions.ui.mappers

import java.time.LocalTime

fun String.toLocalTime(): LocalTime? {
    if (!this.contains(":"))
        return null
    val hoursMinutes = this.split(":", limit = 2)
    val hour = hoursMinutes[0].replace(Regex("[^0-9]"), "").take(2).toInt()
    val minute = hoursMinutes[1].replace(Regex("[^0-9]"), "").take(2).toInt()
    if (hour >= 24 || minute >= 60)
        return null
    return LocalTime.of(hour, minute)
}
