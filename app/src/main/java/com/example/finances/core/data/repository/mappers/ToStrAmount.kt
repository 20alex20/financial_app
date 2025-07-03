package com.example.finances.core.data.repository.mappers

import com.example.finances.core.data.repository.models.Currency
import kotlin.math.roundToInt

fun Double.toStrAmount(currency: Currency): String {
    return this
        .roundToInt()
        .toString()
        .reversed()
        .chunked(3)
        .joinToString(" ")
        .replace(" -", "-")
        .reversed() + " " + currency.symbol
}
