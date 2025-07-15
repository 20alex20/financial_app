package com.example.finances.features.transactions.navigation

sealed class ScreenType(val isIncome: Boolean) {
    data object Expenses : ScreenType(false)
    data object Income : ScreenType(true)

    companion object {
        fun fromBoolean(isIncome: Boolean) = if (isIncome) Income else Expenses
    }
}
