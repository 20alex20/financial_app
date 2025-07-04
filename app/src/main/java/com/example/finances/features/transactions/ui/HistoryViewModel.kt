package com.example.finances.features.transactions.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.finances.core.ReloadEvent
import com.example.finances.core.domain.DateTimeFormatters
import com.example.finances.core.data.Response
import com.example.finances.core.domain.ConvertAmountUseCase
import com.example.finances.features.account.data.AccountRepoImpl
import com.example.finances.core.navigation.NavRoutes
import com.example.finances.core.ui.viewmodel.BaseViewModel
import com.example.finances.core.ui.viewmodel.ViewModelFactory
import com.example.finances.features.transactions.data.TransactionsRepoImpl
import com.example.finances.features.transactions.domain.LoadCurrencyUseCase
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
class HistoryViewModel private constructor(
    private val transactionsRepo: TransactionsRepo,
    private val isIncome: Boolean
) : BaseViewModel() {
    private val _convertAmountUseCase = ConvertAmountUseCase()
    private val _loadCurrencyUseCase = LoadCurrencyUseCase(transactionsRepo)
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
        val asyncCurrency = viewModelScope.async { _loadCurrencyUseCase() }
        val response = transactionsRepo.getTransactions(
            _dates.value.start,
            _dates.value.end,
            isIncome
        )
        when (response) {
            is Response.Failure -> setError()
            is Response.Success -> {
                resetLoadingAndError()
                val currency = asyncCurrency.await()
                _state.value = HistoryViewModelState(
                    total = _convertAmountUseCase(response.data.sumOf { it.amount }, currency),
                    history = response.data.map { it.toHistoryRecord(currency, _today) }
                )
            }
        }
    }

    override suspend fun handleReloadEvent(reloadEvent: ReloadEvent) {
        when (reloadEvent) {
            ReloadEvent.AccountUpdated -> {
                val newCurrency = _loadCurrencyUseCase()
                _state.value = HistoryViewModelState(
                    total = _convertAmountUseCase(_state.value.total, newCurrency),
                    history = _state.value.history.map { expenseIncome ->
                        expenseIncome.copy(
                            amount = _convertAmountUseCase(expenseIncome.amount, newCurrency)
                        )
                    }
                )
            }
        }
    }

    init {
        setPeriod(_dates.value.start, _dates.value.end)
        reloadData()
        observeReloadEvents()
    }

    /**
     * Фабрика по созданию ViewModel экрана истории и прокидывания в нее репозитория
     */
    class Factory(parentRoute: String) : ViewModelFactory<HistoryViewModel>(
        viewModelClass = HistoryViewModel::class.java,
        viewModelInit = {
            HistoryViewModel(
                transactionsRepo = TransactionsRepoImpl(AccountRepoImpl.init()),
                isIncome = parentRoute == NavRoutes.Income.route
            )
        }
    )
}
