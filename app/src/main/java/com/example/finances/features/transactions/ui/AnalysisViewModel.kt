package com.example.finances.features.transactions.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.finances.core.utils.viewmodel.ReloadEvent
import com.example.finances.features.transactions.domain.DateTimeFormatters
import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.usecases.ConvertAmountUseCase
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.features.transactions.navigation.ScreenType
import com.example.finances.features.transactions.domain.usecases.LoadCurrencyUseCase
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import com.example.finances.features.transactions.domain.usecases.GroupByCategoriesUseCase
import com.example.finances.features.transactions.ui.models.AnalysisViewModelState
import com.example.finances.features.transactions.ui.models.DatesViewModelState
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import java.time.LocalDate
import javax.inject.Inject

/**
 * Вьюмодель экрана истории
 */
open class AnalysisViewModel @Inject constructor(
    private val transactionsRepo: TransactionsRepo,
    private val convertAmountUseCase: ConvertAmountUseCase,
    private val loadCurrencyUseCase: LoadCurrencyUseCase,
    private val groupByCategoriesUseCase: GroupByCategoriesUseCase
) : BaseViewModel() {
    private val _screenTypeLatch = CompletableDeferred<ScreenType>()
    private var _today = LocalDate.now()

    private val _dates = mutableStateOf(
        DatesViewModelState(_today.withDayOfMonth(1), _today, "01.01.2025", "00:00")
    )
    val dates: State<DatesViewModelState> = _dates

    private val _state = mutableStateOf(AnalysisViewModelState("0 ₽", emptyList()))
    val state: State<AnalysisViewModelState> = _state

    fun setPeriod(start: LocalDate, end: LocalDate) {
        _today = LocalDate.now()
        val endDate = if (end <= _today) end else _today
        val startDate = if (start <= endDate) start else endDate

        _dates.value = DatesViewModelState(
            start = startDate,
            end = endDate,
            strStart = startDate.format(DateTimeFormatters.date),
            strEnd = endDate.format(DateTimeFormatters.date)
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
                val total = response.data.sumOf { it.amount }
                _state.value = AnalysisViewModelState(
                    total = convertAmountUseCase(total, asyncCurrency.await()),
                    analysis = groupByCategoriesUseCase(response.data, total, currency)
                )
                resetLoadingAndError()
            }
        }
    }

    override suspend fun handleReloadEvent(reloadEvent: ReloadEvent) {
        when (reloadEvent) {
            ReloadEvent.AccountUpdated -> {
                val newCurrency = loadCurrencyUseCase()
                _state.value = AnalysisViewModelState(
                    total = convertAmountUseCase(_state.value.total, newCurrency),
                    analysis = _state.value.analysis.map { analysisCategory ->
                        analysisCategory.copy(
                            amount = convertAmountUseCase(analysisCategory.amount, newCurrency)
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
