package com.example.finances.core.data.repository.models

import kotlin.math.roundToInt

/**
 * Перечисление поддерживаемых типов валют (объекты передают информацию о используемом типе валют)
 */
enum class Currency(val symbol: String) {
    RUBLE("₽"),
    DOLLAR("$"),
    EURO("€");

    fun getStrAmount(value: Double): String {
        return value
            .roundToInt()
            .toString()
            .reversed()
            .chunked(DIGITS_NUMBER)
            .joinToString(" ")
            .replace(" -", "-")
            .reversed() + " " + symbol
    }

    companion object {
        fun parseStr(currencyStr: String): Currency = when (currencyStr) {
            "RUB" -> RUBLE
            "USD" -> DOLLAR
            "EUR" -> EURO
            else -> RUBLE
        }

        const val DIGITS_NUMBER = 3
    }
}
