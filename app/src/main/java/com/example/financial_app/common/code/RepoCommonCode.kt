package com.example.financial_app.common.code

import com.example.financial_app.common.models.Currency
import kotlin.math.roundToInt

fun getStringAmount(value: Double, currency: Currency): String {
    return value
        .roundToInt()
        .toString()
        .reversed()
        .chunked(3)
        .joinToString(" ")
        .replace(" -", "-")
        .reversed() + " " + currency.symbol
}
