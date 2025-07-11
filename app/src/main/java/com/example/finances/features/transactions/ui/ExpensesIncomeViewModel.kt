package com.example.finances.features.transactions.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.finances.core.utils.viewmodel.ReloadEvent
import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.usecases.ConvertAmountUseCase
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.features.transactions.domain.usecases.LoadCurrencyUseCase
import com.example.finances.features.transactions.ui.mappers.toExpenseIncome
import com.example.finances.features.transactions.ui.models.ExpensesIncomeViewModelState
import kotlinx.coroutines.async
import java.time.LocalDate

/**
 * Вьюмодель экрана расходов/доходов
 */
open class ExpensesIncomeViewModel(
    private val isIncome: Boolean,
    private val transactionsRepo: TransactionsRepo,
    private val convertAmountUseCase: ConvertAmountUseCase,
    private val loadCurrencyUseCase: LoadCurrencyUseCase
) : BaseViewModel() {

    private val _state = mutableStateOf(ExpensesIncomeViewModelState("0 ₽", emptyList()))
    val state: State<ExpensesIncomeViewModelState> = _state

    override suspend fun loadData() {
        val asyncCurrency = viewModelScope.async { loadCurrencyUseCase() }
        val today = LocalDate.now()
        when (val response = transactionsRepo.getTransactions(today, today, isIncome)) {
            is Response.Failure -> setError()
            is Response.Success -> {
                val currency = asyncCurrency.await()
                resetLoadingAndError()
                _state.value = ExpensesIncomeViewModelState(
                    total = convertAmountUseCase(response.data.sumOf { it.amount }, currency),
                    expensesIncome = response.data.map { it.toExpenseIncome(currency) }
                )
            }
        }
    }

    override suspend fun handleReloadEvent(reloadEvent: ReloadEvent) {
        when (reloadEvent) {
            ReloadEvent.AccountUpdated -> {
                val newCurrency = loadCurrencyUseCase()
                _state.value = ExpensesIncomeViewModelState(
                    total = convertAmountUseCase(_state.value.total, newCurrency),
                    expensesIncome = _state.value.expensesIncome.map { expenseIncome ->
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
        reloadData()
        observeReloadEvents()
    }
}
