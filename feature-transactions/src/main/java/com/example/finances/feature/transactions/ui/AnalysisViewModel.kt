package com.example.finances.feature.transactions.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.finances.core.managers.ConvertAmountUseCase
import com.example.finances.core.utils.viewmodel.ReloadEvent
import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.feature.transactions.domain.DateTimeFormatters
import com.example.finances.feature.transactions.domain.repository.TransactionsRepo
import com.example.finances.feature.transactions.domain.usecases.GroupByCategoriesUseCase
import com.example.finances.feature.transactions.domain.usecases.LoadCurrencyUseCase
import com.example.finances.feature.transactions.navigation.ScreenType
import com.example.finances.feature.transactions.ui.mappers.toAnalysisCategory
import com.example.finances.feature.transactions.ui.mappers.toShortAnalysisRecord
import com.example.finances.feature.transactions.ui.models.AnalysisViewModelState
import com.example.finances.feature.transactions.ui.models.DatesViewModelState
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import java.time.LocalDate
import javax.inject.Inject

/**
 * Вьюмодель экрана анализа
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

    private val _state = mutableStateOf(AnalysisViewModelState("0 ₽", emptyList(), emptyList()))
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
        val type = _screenTypeLatch.await()
        val response = transactionsRepo.getTransactions(_dates.value.start, _dates.value.end, type)
        when (response) {
            is Response.Failure -> setError()
            is Response.Success -> {
                val total = response.data.sumOf { it.amount }
                val analysisGroups = groupByCategoriesUseCase(response.data, total)
                _state.value = AnalysisViewModelState(
                    total = convertAmountUseCase(total, asyncCurrency.await()),
                    pieChartData = analysisGroups.map { it.toShortAnalysisRecord() },
                    analysis = analysisGroups.map {
                        it.toAnalysisCategory(convertAmountUseCase, asyncCurrency.await())
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
                _state.value = AnalysisViewModelState(
                    total = convertAmountUseCase(_state.value.total, newCurrency),
                    pieChartData = _state.value.pieChartData,
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
