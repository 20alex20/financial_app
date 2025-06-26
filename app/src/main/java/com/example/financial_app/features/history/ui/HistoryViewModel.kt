package com.example.financial_app.features.history.ui

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.financial_app.common.models.Response
import com.example.financial_app.features.history.data.HistoryRepoImpl
import com.example.financial_app.features.history.ui.models.HistoryRecordUiModel
import com.example.financial_app.features.navigation.data.NavRoutes
import com.example.financial_app.common.models.Currency
import com.example.financial_app.features.history.domain.repo.HistoryRepo
import com.example.financial_app.features.account.data.AccountRepoImpl
import com.example.financial_app.features.history.domain.models.HistoryRecord
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.lastOrNull
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class HistoryViewModel(
    private val historyRepo: HistoryRepo,
    private val isIncome: Boolean
) : ViewModel() {
    private var _loadedJob: Job? = null

    private var _startDate = mutableStateOf(firstDayOfYear)
    val startDate: State<LocalDate> = _startDate

    private var _endDate = mutableStateOf(firstDayOfYear)
    val endDate: State<LocalDate> = _endDate

    private val _strStart = mutableStateOf("Январь 2025")
    val strStart: State<String> = _strStart

    private val _strEnd = mutableStateOf("00:00")
    val strEnd: State<String> = _strEnd

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _error = mutableStateOf(false)
    val error: State<Boolean> = _error

    private val _history = mutableStateOf(listOf<HistoryRecordUiModel>())
    val history: State<List<HistoryRecordUiModel>> = _history

    private val _total = mutableStateOf("0 ₽")
    val total: State<String> = _total

    private suspend fun getCurrency(): Currency {
        val currencyResponse = historyRepo.getCurrency().lastOrNull()
        return if (currencyResponse is Response.Success)
            currencyResponse.data
        else
            Currency.RUBLE
    }

    private fun convertRecord(
        record: HistoryRecord,
        currency: Currency,
        today: LocalDate
    ): HistoryRecordUiModel {
        val dateTime = if (record.dateTime.toLocalDate() == today)
            record.dateTime.format(timeFormatter)
        else
            record.dateTime.format(dateFormatter)

        return HistoryRecordUiModel(
            id = record.id,
            categoryName = record.categoryName,
            categoryEmoji = record.categoryEmoji,
            dateTime = dateTime,
            amount = currency.getStrAmount(record.amount),
            comment = record.comment
        )
    }

    private fun showError() {
        _loading.value = false
        _error.value = true
    }

    private fun loadHistoryRecordsAndTotal(today: LocalDate) = viewModelScope.launch {
        try {
            _error.value = false
            val currency = getCurrency()
            historyRepo.getHistory(_startDate.value, _endDate.value, isIncome).collect { response ->
                when (response) {
                    is Response.Loading -> _loading.value = true
                    is Response.Success -> {
                        _loading.value = false
                        _error.value = false
                        _history.value = response.data.map { convertRecord(it, currency, today) }
                        _total.value = currency.getStrAmount(response.data.sumOf { it.amount })
                    }
                    is Response.Failure -> showError()
                }
            }
        } catch (e: Exception) {
            showError()
        }
    }.also { _loadedJob = it }

    private fun updateDates() {
        val todayWithTime = LocalDateTime.now()
        _strEnd.value = if (_endDate.value != todayWithTime.toLocalDate())
            _endDate.value.format(dateFormatter)
        else
            todayWithTime.format(timeFormatter)

        _strStart.value = if (_startDate.value.dayOfMonth != 1)
            _startDate.value.format(dateFormatter)
        else
            _startDate.value.month.getDisplayName(TextStyle.FULL, locale).replaceFirstChar { char ->
                if (char.isLowerCase())
                    char.titlecase(locale)
                else
                    char.toString()
            } + " " + _startDate.value.year
    }

    fun loadWithNewDates(start: LocalDate, end: LocalDate) {
        _loadedJob?.cancel()
        _loading.value = false

        val today = LocalDate.now()
        _endDate.value = if (end <= today) end else today
        _startDate.value = if (start <= _endDate.value) start else _endDate.value

        updateDates()
        loadHistoryRecordsAndTotal(today)
    }

    init {
        val today = LocalDate.now()
        loadWithNewDates(today.withDayOfMonth(1), today)
    }

    override fun onCleared() {
        super.onCleared()
        _loadedJob?.cancel()
        viewModelScope.cancel()
    }

    class Factory(
        private val context: Context,
        private val parentRoute: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HistoryViewModel::class.java))
                return HistoryViewModel(
                    historyRepo = HistoryRepoImpl(context, AccountRepoImpl.init(context)),
                    isIncome = parentRoute == NavRoutes.Income.route
                ) as T
            throw Exception(UNKNOWN_VIEWMODEL)
        }
    }

    companion object {
        private val firstDayOfYear = LocalDate.of(2025, 1, 1)
        private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        private val locale = Locale("ru")

        private const val UNKNOWN_VIEWMODEL = "Unknown ViewModel class"
    }
}
