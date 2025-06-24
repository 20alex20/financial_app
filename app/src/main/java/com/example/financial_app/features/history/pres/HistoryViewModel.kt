package com.example.financial_app.features.history.pres

import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.financial_app.R
import com.example.financial_app.common.models.Response
import com.example.financial_app.common.usecase.ShowToastUseCase
import com.example.financial_app.features.history.data.HistoryRepo
import com.example.financial_app.features.history.pres.models.HistoryRecordUiModel
import com.example.financial_app.features.navigation.data.NavRoutes
import com.example.financial_app.features.network.data.models.Currency
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class HistoryViewModel(
    application: Application,
    isIncome: Boolean
) : AndroidViewModel(application) {
    private val historyRepo = HistoryRepo(application, isIncome)
    private val showToast = ShowToastUseCase(application)
    private var loadJob: Job? = null
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val ruLocale = Locale("ru")

    private var _startDate = mutableStateOf(LocalDate.now().withDayOfMonth(1))
    val startDate: State<LocalDate> = _startDate

    private val _strStart = mutableStateOf("Январь 2025")
    val strStart: State<String> = _strStart

    private var _endDate = mutableStateOf(LocalDate.now())
    val endDate: State<LocalDate> = _endDate

    private val _strEnd = mutableStateOf("00:00")
    val strEnd: State<String> = _strEnd

    private val _total = mutableStateOf("0 ₽")
    val total: State<String> = _total

    private val _history = mutableStateOf(listOf<HistoryRecordUiModel>())
    val history: State<List<HistoryRecordUiModel>> = _history

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private fun loadHistoryRecordsAndTotal() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            try {
                val currencyResponse = historyRepo.getCurrency()
                val currency = if (currencyResponse is Response.Success)
                    currencyResponse.data
                else
                    Currency.RUBLE

                historyRepo.clearCache()
                historyRepo.getHistory(_startDate.value, _endDate.value).collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true

                        is Response.Success -> {
                            val today = LocalDate.now()
                            _history.value = response.data.map { record ->
                                val dateTime = if (record.dateTime.toLocalDate() == today)
                                    record.dateTime.format(timeFormatter)
                                else
                                    record.dateTime.format(dateFormatter)
                                HistoryRecordUiModel(
                                    id = record.id,
                                    categoryName = record.categoryName,
                                    categoryEmoji = record.categoryEmoji,
                                    dateTime = dateTime,
                                    amount = currency.getStrAmount(record.amount),
                                    comment = record.comment
                                )
                            }
                            _total.value = currency.getStrAmount(response.data.sumOf { record ->
                                record.amount
                            })
                        }

                        is Response.Failure -> {
                            showToast(application.getString(R.string.error_data_loading))
                        }
                    }
                }
            } catch (e: Exception) {
                // showToast(application.getString(R.string.error_data_processing))
            }
        }
        _loading.value = false
    }

    private fun updateDates() {
        val today = LocalDateTime.now()
        if (_endDate.value != today.toLocalDate())
            _strEnd.value = _endDate.value.format(dateFormatter)
        else
            _strEnd.value = today.format(timeFormatter)

        if (_startDate.value.dayOfMonth != 1)
            _strStart.value = _startDate.value.format(dateFormatter)
        else
            _strStart.value = buildString {
                append(
                    _startDate.value.month
                        .getDisplayName(TextStyle.FULL, ruLocale)
                        .replaceFirstChar { symbol ->
                            if (symbol.isLowerCase())
                                symbol.titlecase(ruLocale)
                            else
                                symbol.toString()
                        }
                )
                append(" ")
                append(_startDate.value.year)
            }
    }

    fun loadWithNewDates(start: LocalDate, end: LocalDate) {
        val today = LocalDate.now()
        _endDate.value = if (end <= today) end else today
        _startDate.value = if (start <= _endDate.value) start else _endDate.value

        updateDates()
        loadHistoryRecordsAndTotal()
    }

    fun refresh() {
        val today = LocalDate.now()
        loadWithNewDates(today.withDayOfMonth(1), today)
    }

    init {
        refresh()
    }

    override fun onCleared() {
        super.onCleared()
        loadJob?.cancel()
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
                    application = context.applicationContext as Application,
                    isIncome = parentRoute == NavRoutes.Income.route
                ) as T
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
