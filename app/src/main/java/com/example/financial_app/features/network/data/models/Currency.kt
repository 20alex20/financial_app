package com.example.financial_app.features.network.data.models

enum class Currency(val symbol: String) {
    RUBLE("₽"),
    DOLLAR("$"),
    EURO("€");

    companion object {
        fun parseStr(currencyStr: String): Currency = when (currencyStr) {
            "RUB" -> RUBLE
            "USD" -> DOLLAR
            "EUR" -> EURO
            else -> RUBLE
        }
    }
}
