package com.example.finances.features.account.data.mappers

import com.example.finances.core.utils.models.Currency
import com.example.finances.core.utils.models.Currency.RUBLE

fun String.toCurrency(): Currency {
    Currency.entries.forEach { currency ->
        if (this == currency.shortName)
            return currency
    }
    return RUBLE
}
