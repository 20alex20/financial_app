package com.example.finances.feature.transactions.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.finances.core.managers.ConvertAmountUseCase
import com.example.finances.core.utils.viewmodel.ReloadEvent
import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.feature.transactions.domain.repository.TransactionsRepo
import com.example.finances.feature.transactions.domain.usecases.LoadCurrencyUseCase
import com.example.finances.feature.transactions.navigation.ScreenType
import com.example.finances.feature.transactions.ui.mappers.toExpenseIncome
import com.example.finances.feature.transactions.ui.models.ExpensesIncomeViewModelState
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import java.time.LocalDate
import javax.inject.Inject

/**
 * Вьюмодель экрана расходов/доходов
 */
open class ExpensesIncomeViewModel @Inject constructor(
    private val transactionsRepo: TransactionsRepo,
    private val convertAmountUseCase: ConvertAmountUseCase,
    private val loadCurrencyUseCase: LoadCurrencyUseCase
) : BaseViewModel() {
    private val _screenTypeLatch = CompletableDeferred<ScreenType>()

    private val _state = mutableStateOf(ExpensesIncomeViewModelState("0 ₽", emptyList()))
    val state: State<ExpensesIncomeViewModelState> = _state

    override suspend fun loadData(scope: CoroutineScope) {
        val asyncCurrency = scope.async { loadCurrencyUseCase() }
        val today = LocalDate.now()
        val response = transactionsRepo.getTransactions(today, today, _screenTypeLatch.await())
        when (response) {
            is Response.Failure -> setError()
            is Response.Success -> {
                val currency = asyncCurrency.await()
                _state.value = ExpensesIncomeViewModelState(
                    total = convertAmountUseCase(response.data.sumOf { it.amount }, currency),
                    expensesIncome = response.data.map { transaction ->
                        transaction.toExpenseIncome(currency, convertAmountUseCase)
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

    override fun setViewModelParams(extras: CreationExtras) {
        if (!_screenTypeLatch.isCompleted) {
            _screenTypeLatch.complete(extras[ViewModelParams.Screen] ?: ScreenType.Expenses)
        }
    }

    init {
        reloadData()
        observeReloadEvents()
    }
}
