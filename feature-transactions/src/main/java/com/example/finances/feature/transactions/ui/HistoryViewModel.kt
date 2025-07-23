package com.example.finances.feature.transactions.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.finances.core.managers.ConvertAmountUseCase
import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.core.utils.viewmodel.ReloadEvent
import com.example.finances.feature.transactions.domain.DateTimeFormatters
import com.example.finances.feature.transactions.domain.repository.TransactionsRepo
import com.example.finances.feature.transactions.domain.usecases.LoadCurrencyUseCase
import com.example.finances.feature.transactions.navigation.ScreenType
import com.example.finances.feature.transactions.ui.mappers.toHistoryRecord
import com.example.finances.feature.transactions.ui.models.DatesViewModelState
import com.example.finances.feature.transactions.ui.models.HistoryViewModelState
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Вьюмодель экрана истории
 */
open class HistoryViewModel @Inject constructor(
    private val transactionsRepo: TransactionsRepo,
    private val convertAmountUseCase: ConvertAmountUseCase,
    private val loadCurrencyUseCase: LoadCurrencyUseCase
) : BaseViewModel() {
    private val _screenTypeLatch = CompletableDeferred<ScreenType>()
    private var _today = LocalDate.now()

    private val _dates = mutableStateOf(
        DatesViewModelState(_today.withDayOfMonth(1), _today, "01.01.2025", "00:00")
    )
    val dates: State<DatesViewModelState> = _dates

    private val _state = mutableStateOf(HistoryViewModelState("0 ₽", emptyList()))
    val state: State<HistoryViewModelState> = _state

    fun setPeriod(start: LocalDate, end: LocalDate) {
        _today = LocalDate.now()
        val endDate = if (end <= _today) end else _today
        val startDate = if (start <= endDate) start else endDate

        _dates.value = DatesViewModelState(
            start = startDate,
            end = endDate,
            strStart = startDate.format(DateTimeFormatters.date),
            strEnd = when (endDate) {
                _today -> LocalDateTime.now().format(DateTimeFormatters.time)
                else -> endDate.format(DateTimeFormatters.date)
            }
        )
    }

    override suspend fun loadData(scope: CoroutineScope) {
        val asyncCurrency = scope.async { loadCurrencyUseCase() }
        val response = transactionsRepo.getTransactions(
            _dates.value.start,
            _dates.value.end,
            _screenTypeLatch.await()
        )
        when (response) {
            is Response.Failure -> setError()
            is Response.Success -> {
                val currency = asyncCurrency.await()
                _state.value = HistoryViewModelState(
                    total = convertAmountUseCase(response.data.sumOf { it.amount }, currency),
                    history = response.data.map { transaction ->
                        transaction.toHistoryRecord(currency, _today, convertAmountUseCase)
                    }
                )
                resetLoadingAndError()
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

    override fun setViewModelParams(extras: CreationExtras) {
        if (!_screenTypeLatch.isCompleted) {
            _screenTypeLatch.complete(extras[ViewModelParams.Screen] ?: ScreenType.Expenses)
        }
    }

    init {
        setPeriod(_dates.value.start, _dates.value.end)
        reloadData()
        observeReloadEvents()
    }
}
