package com.example.finances.core.managers

import com.example.finances.core.di.CoreScope
import com.example.finances.core.utils.models.Currency
import javax.inject.Inject
import kotlin.math.roundToLong

@CoreScope
class ConvertAmountUseCase @Inject constructor() {
    operator fun invoke(value: String): Double {
        var amount = 0.0
        val digits = value.replace(Regex("[^0-9]"), "")
        if (digits.isNotEmpty()) {
            amount = digits.take(MAX_AMOUNT_LENGTH).toDouble()
            val index = value.indexOfFirst { it.isDigit() || it == '-' }
            if (value[index] == '-')
                amount *= -1
        }
        return amount
    }

    operator fun invoke(value: Double, currency: Currency): String {
        return value
            .roundToLong()
            .toString()
            .reversed()
            .chunked(3)
            .joinToString(" ")
            .replace(" -", "-")
            .reversed() + " " + currency.symbol
    }

    operator fun invoke(value: String, newCurrency: Currency): String {
        return value.dropLast(1) + newCurrency.symbol
    }

    companion object {
        private const val MAX_AMOUNT_LENGTH = 12
    }
}
