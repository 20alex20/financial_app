package com.example.financial_app.common.models

import kotlin.math.roundToInt

enum class Currency(val symbol: String) {
    RUBLE("₽"),
    DOLLAR("$"),
    EURO("€");

    fun getStrAmount(value: Double): String {
        return value
            .roundToInt()
            .toString()
            .reversed()
            .chunked(3)
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
    }
}
