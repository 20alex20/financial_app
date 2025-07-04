package com.example.finances.core.data.mappers

import com.example.finances.core.domain.models.Currency
import com.example.finances.core.domain.models.Currency.RUBLE

fun String.toCurrency(): Currency {
    Currency.entries.forEach { currency ->
        if (this == currency.shortName)
            return currency
    }
    return RUBLE
}
