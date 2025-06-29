package com.example.finances.features.transactions.ui.models

import java.time.LocalDate

data class HistoryDatesViewModelState(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val strStart: String,
    val strEnd: String
)
