package com.example.finances.features.account.domain.mappers

fun String.toAmount(): Double {
    var amount = 0.0
    val digits = replace(Regex("[^0-9]"), "")
    if (digits.isNotEmpty()) {
        amount = digits.toDouble()
        val index = indexOfFirst { it.isDigit() || it == '-' }
        if (this[index] == '-')
            amount *= -1
    }
    return amount
}
