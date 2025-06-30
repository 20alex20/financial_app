package com.example.finances.features.transactions.ui.models

import java.time.LocalDate

data class HistoryDatesViewModelState(
    val start: LocalDate,
    val end: LocalDate,
    val strStart: String,
    val strEnd: String
)
