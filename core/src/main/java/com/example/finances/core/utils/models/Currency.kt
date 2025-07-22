package com.example.finances.core.utils.models

/**
 * Перечисление поддерживаемых типов валют (объекты передают информацию о используемом типе валют)
 */
enum class Currency(val symbol: String, val shortName: String) {
    RUBLE("₽", "RUB"),
    DOLLAR("$", "USD"),
    EURO("€", "EUR");

    companion object {
        fun fromShortName(shortName: String): Currency {
            Currency.entries.forEach { currency ->
                if (shortName == currency.shortName)
                    return currency
            }
            return RUBLE
        }
    }
}
