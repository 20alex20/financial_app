package com.example.financial_app.features.history.pres

import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.financial_app.common.code.getStringAmount
import com.example.financial_app.features.history.data.HistoryRepo
import com.example.financial_app.features.history.pres.models.HistoryRecordUiModel
import com.example.financial_app.features.navigation.data.NavRoutes
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class HistoryViewModel(application: Application, isIncome: Boolean) : ViewModel() {
    private val historyRepo = HistoryRepo(application, isIncome)
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val ruLocale = Locale("ru")

    private val _startDateTime = mutableStateOf("Январь 2025")
    val startDateTime: State<String> = _startDateTime

    private val _endDateTime = mutableStateOf("00:00")
    val endDateTime: State<String> = _endDateTime

    private val _total = mutableStateOf("0 ₽")
    val total: State<String> = _total

    private val _history = mutableStateOf(listOf<HistoryRecordUiModel>())
    val history: State<List<HistoryRecordUiModel>> = _history

    private fun loadHistoryRecordsAndTotal() {
        viewModelScope.launch {
            try {
                val currency = historyRepo.getCurrency()
                val repoHistory = historyRepo.getHistory()
                val today = LocalDate.now()

                _history.value = repoHistory.map { record ->
                    val dateTime = if (record.dateTime.toLocalDate() == today)
                        record.dateTime.format(timeFormatter)
                    else
                        record.dateTime.format(dateFormatter)

                    HistoryRecordUiModel(
                        id = record.id,
                        categoryName = record.categoryName,
                        categoryEmoji = record.categoryEmoji,
                        dateTime = dateTime,
                        amount = getStringAmount(record.amount, currency),
                        comment = record.comment
                    )
                }

                _total.value = getStringAmount(repoHistory.sumOf { it.amount }, currency)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun updateDateTime() {
        val now = LocalDateTime.now()

        _startDateTime.value = buildString {
            append(now.month.getDisplayName(TextStyle.FULL, ruLocale).replaceFirstChar { symbol ->
                if (symbol.isLowerCase())
                    symbol.titlecase(ruLocale)
                else
                    symbol.toString()
            })
            append(" ")
            append(now.year)
        }

        _endDateTime.value = now.format(timeFormatter)
    }

    fun refresh() {
        loadHistoryRecordsAndTotal()
        updateDateTime()
    }

    init {
        refresh()
    }

    class Factory(
        private val context: Context,
        private val parentRoute: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HistoryViewModel::class.java))
                return HistoryViewModel(
                    application = context.applicationContext as Application,
                    isIncome = parentRoute == NavRoutes.Income.route
                ) as T
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
