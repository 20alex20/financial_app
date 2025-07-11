package com.example.finances.features.transactions.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.finances.core.utils.viewmodel.ReloadEvent
import com.example.finances.features.transactions.domain.DateTimeFormatters
import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.usecases.ConvertAmountUseCase
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.features.transactions.domain.usecases.LoadCurrencyUseCase
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import com.example.finances.features.transactions.ui.mappers.toHistoryRecord
import com.example.finances.features.transactions.ui.models.HistoryDatesViewModelState
import com.example.finances.features.transactions.ui.models.HistoryViewModelState
import kotlinx.coroutines.async
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Вьюмодель экрана истории
 */
open class HistoryViewModel(
    private val isIncome: Boolean,
    private val transactionsRepo: TransactionsRepo,
    private val convertAmountUseCase: ConvertAmountUseCase,
    private val loadCurrencyUseCase: LoadCurrencyUseCase
) : BaseViewModel() {
    private var _today = LocalDate.now()

    private val _dates = mutableStateOf(
        HistoryDatesViewModelState(_today.withDayOfMonth(1), _today, "01.01.2025", "00:00")
    )
    val dates: State<HistoryDatesViewModelState> = _dates

    private val _state = mutableStateOf(HistoryViewModelState("0 ₽", emptyList()))
    val state: State<HistoryViewModelState> = _state

    fun setPeriod(start: LocalDate, end: LocalDate) {
        _today = LocalDate.now()
        val endDate = if (end <= _today) end else _today
        val startDate = if (start <= endDate) start else endDate

        _dates.value = HistoryDatesViewModelState(
            start = startDate,
            end = endDate,
            strStart = startDate.format(DateTimeFormatters.date),
            strEnd = when (endDate) {
                _today -> LocalDateTime.now().format(DateTimeFormatters.time)
                else -> endDate.format(DateTimeFormatters.date)
            }
        )
    }

    override suspend fun loadData() {
        val asyncCurrency = viewModelScope.async { loadCurrencyUseCase() }
        val response = transactionsRepo.getTransactions(
            _dates.value.start,
            _dates.value.end,
            isIncome
        )
        when (response) {
            is Response.Failure -> setError()
            is Response.Success -> {
                val currency = asyncCurrency.await()
                resetLoadingAndError()
                _state.value = HistoryViewModelState(
                    total = convertAmountUseCase(response.data.sumOf { it.amount }, currency),
                    history = response.data.map { it.toHistoryRecord(currency, _today) }
                )
            }
        }
    }

    override suspend fun handleReloadEvent(reloadEvent: ReloadEvent) {
        when (reloadEvent) {
            ReloadEvent.AccountUpdated -> {
                val newCurrency = loadCurrencyUseCase()
                _state.value = HistoryViewModelState(
                    total = convertAmountUseCase(_state.value.total, newCurrency),
                    history = _state.value.history.map { expenseIncome ->
                        expenseIncome.copy(
                            amount = convertAmountUseCase(expenseIncome.amount, newCurrency)
                        )
                    }
                )
            }
            ReloadEvent.TransactionCreatedUpdated -> {
                reloadData()
            }
        }
    }

    init {
        setPeriod(_dates.value.start, _dates.value.end)
        reloadData()
        observeReloadEvents()
    }
}
