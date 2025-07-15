package com.example.finances.features.transactions.domain

sealed interface ScreenType {
    data object Expenses : ScreenType
    data object Income : ScreenType
}
