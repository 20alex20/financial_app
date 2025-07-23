package com.example.finances.feature.transactions.ui.models

import java.time.LocalDate

data class DatesViewModelState(
    val start: LocalDate,
    val end: LocalDate,
    val strStart: String,
    val strEnd: String
)
