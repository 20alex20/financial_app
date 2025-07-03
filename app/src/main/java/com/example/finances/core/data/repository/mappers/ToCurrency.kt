package com.example.finances.core.data.repository.mappers

import com.example.finances.core.data.repository.models.Currency
import com.example.finances.core.data.repository.models.Currency.DOLLAR
import com.example.finances.core.data.repository.models.Currency.EURO
import com.example.finances.core.data.repository.models.Currency.RUBLE

fun String.toCurrency(): Currency {
    return when (this) {
        RUBLE.shortName -> RUBLE
        DOLLAR.shortName -> DOLLAR
        EURO.shortName -> EURO
        else -> RUBLE
    }
}
