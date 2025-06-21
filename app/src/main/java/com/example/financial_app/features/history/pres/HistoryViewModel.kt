package com.example.financial_app.features.history.pres

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.financial_app.features.history.domain.models.HistoryRecord
import androidx.compose.runtime.State
import com.example.financial_app.features.history.data.HistoryRepo

class HistoryViewModel : ViewModel() {
    private val _startDateTime = mutableStateOf("Январь 2025")
    val startDateTime: State<String> = _startDateTime

    private val _endDateTime = mutableStateOf("00:00")
    val endDateTime: State<String> = _endDateTime

    private val _sum = mutableStateOf("0 ₽")
    val sum: State<String> = _sum

    private val _history = mutableStateOf(listOf<HistoryRecord>())
    val history: State<List<HistoryRecord>> = _history

    private fun loadStartDateTime() {
        _startDateTime.value = HistoryRepo.getStartDateTime()
    }

    private fun loadEndDateTime() {
        _endDateTime.value = HistoryRepo.getEndDateTime()
    }

    private fun loadSum() {
        _sum.value = HistoryRepo.getSum()
    }

    private fun loadHistory() {
        _history.value = HistoryRepo.getHistory()
    }

    init {
        loadStartDateTime()
        loadEndDateTime()
        loadSum()
        loadHistory()
    }
}
