package com.example.finances.core.domain.models

/**
 * Перечисление поддерживаемых типов валют (объекты передают информацию о используемом типе валют)
 */
enum class Currency(val symbol: String, val shortName: String) {
    RUBLE("₽", "RUB"),
    DOLLAR("$", "USD"),
    EURO("€", "EUR")
}
