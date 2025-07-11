package com.example.finances.core.utils.models

/**
 * Enumeration of supported currency types (objects provide information about the currency type used)
 */
enum class Currency(val symbol: String, val shortName: String) {
    RUBLE("₽", "RUB"),
    DOLLAR("$", "USD"),
    EURO("€", "EUR")
}
