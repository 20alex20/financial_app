package com.example.finances.core.domain

import com.example.finances.core.domain.models.Currency
import kotlin.math.roundToInt

class ConvertAmountUseCase {
    operator fun invoke(value: String): Double {
        var amount = 0.0
        val digits = value.replace(Regex("[^0-9]"), "")
        if (digits.isNotEmpty()) {
            amount = digits.toDouble()
            val index = value.indexOfFirst { it.isDigit() || it == '-' }
            if (value[index] == '-')
                amount *= -1
        }
        return amount
    }

    operator fun invoke(value: Double, currency: Currency): String {
        return value
            .roundToInt()
            .toString()
            .reversed()
            .chunked(DIGITS_NUMBER)
            .joinToString(" ")
            .replace(" -", "-")
            .reversed() + " " + currency.symbol
    }

    companion object {
        private const val DIGITS_NUMBER = 3
    }
}
