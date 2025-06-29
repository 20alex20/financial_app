package com.example.finances.features.transactions.ui

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.finances.features.transactions.domain.DateTimeFormatters
import com.example.finances.core.data.network.models.Response
import com.example.finances.core.data.repository.models.Currency
import com.example.finances.features.account.data.AccountRepoImpl
import com.example.finances.features.transactions.ui.models.HistoryRecord
import com.example.finances.core.navigation.NavRoutes
import com.example.finances.core.ui.viewmodel.BaseViewModel
import com.example.finances.core.ui.viewmodel.ViewModelFactory
import com.example.finances.features.transactions.data.TransactionsRepoImpl
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import com.example.finances.features.transactions.ui.mappers.toHistoryRecord
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * ViewModel экрана истории
 */
class HistoryViewModel(
    private val transactionsRepo: TransactionsRepo,
    private val isIncome: Boolean
) : BaseViewModel() {
    private var today: LocalDate = LocalDate.now()
    private var _loadedJob: Job? = null

    private var _startDate = mutableStateOf(today.withDayOfMonth(1))
    val startDate: State<LocalDate> = _startDate

    private var _endDate = mutableStateOf(today)
    val endDate: State<LocalDate> = _endDate

    private val _strStart = mutableStateOf("01.01.2025")
    val strStart: State<String> = _strStart

    private val _strEnd = mutableStateOf("00:00")
    val strEnd: State<String> = _strEnd

    private val _history = mutableStateOf(listOf<HistoryRecord>())
    val history: State<List<HistoryRecord>> = _history

    private val _total = mutableStateOf("0 ₽")
    val total: State<String> = _total

    override fun loadData() = viewModelScope.launch {
        try {
            val currency = transactionsRepo.getCurrency().lastOrNull()
                .let { if (it is Response.Success) it.data else Currency.RUBLE }
            transactionsRepo.getTransactions(_startDate.value, _endDate.value, isIncome)
                .collect { reply ->
                    when (reply) {
                        is Response.Loading -> setLoading()
                        is Response.Failure -> setError()
                        is Response.Success -> {
                            resetLoadingAndError()
                            _history.value = reply.data.map { it.toHistoryRecord(currency, today) }
                            _total.value = currency.getStrAmount(reply.data.sumOf { it.amount })
                        }
                    }
                }
        } catch (_: Exception) {
            setError()
        }
    }.also { _loadedJob = it }

    fun setPeriod(start: LocalDate, end: LocalDate) {
        today = LocalDate.now()
        _endDate.value = if (end <= today) end else today
        _startDate.value = if (start <= _endDate.value) start else _endDate.value

        _strEnd.value = if (_endDate.value != today)
            _endDate.value.format(DateTimeFormatters.date)
        else
            LocalDateTime.now().format(DateTimeFormatters.time)
        _strStart.value = _startDate.value.format(DateTimeFormatters.date)

        _loadedJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        _loadedJob?.cancel()
    }

    /**
     * Фабрика по созданию ViewModel экрана истории и прокидывания в нее репозитория
     */
    class Factory(context: Context, parentRoute: String) : ViewModelFactory<HistoryViewModel>(
        viewModelClass = HistoryViewModel::class.java,
        viewModelInit = {
            HistoryViewModel(
                transactionsRepo = TransactionsRepoImpl(context, AccountRepoImpl.init(context)),
                isIncome = parentRoute == NavRoutes.Income.route
            )
        }
    )
}
