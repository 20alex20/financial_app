package com.example.finances.features.transactions.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.finances.core.buses.ReloadEvent
import com.example.finances.core.data.Response
import com.example.finances.core.domain.ConvertAmountUseCase
import com.example.finances.features.account.data.AccountRepoImpl
import com.example.finances.core.navigation.NavRoutes
import com.example.finances.features.transactions.data.TransactionsRepoImpl
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import com.example.finances.core.ui.viewmodel.BaseViewModel
import com.example.finances.core.ui.viewmodel.ViewModelFactory
import com.example.finances.features.transactions.domain.LoadCurrencyUseCase
import com.example.finances.features.transactions.ui.mappers.toExpenseIncome
import com.example.finances.features.transactions.ui.models.ExpensesIncomeViewModelState
import kotlinx.coroutines.async
import java.time.LocalDate

/**
 * Вьюмодель экрана расходов/доходов
 */
class ExpensesIncomeViewModel private constructor(
    private val transactionsRepo: TransactionsRepo,
    private val isIncome: Boolean
) : BaseViewModel() {
    private val _convertAmountUseCase = ConvertAmountUseCase()
    private val _loadCurrencyUseCase = LoadCurrencyUseCase(transactionsRepo)

    private val _state = mutableStateOf(ExpensesIncomeViewModelState("0 ₽", emptyList()))
    val state: State<ExpensesIncomeViewModelState> = _state

    override suspend fun loadData() {
        val asyncCurrency = viewModelScope.async { _loadCurrencyUseCase() }
        val today = LocalDate.now()
        when (val response = transactionsRepo.getTransactions(today, today, isIncome)) {
            is Response.Failure -> setError()
            is Response.Success -> {
                resetLoadingAndError()
                val currency = asyncCurrency.await()
                _state.value = ExpensesIncomeViewModelState(
                    total = _convertAmountUseCase(response.data.sumOf { it.amount }, currency),
                    expensesIncome = response.data.map { it.toExpenseIncome(currency) }
                )
            }
        }
    }

    override suspend fun handleReloadEvent(reloadEvent: ReloadEvent) {
        when (reloadEvent) {
            ReloadEvent.AccountUpdated -> {
                val newCurrency = _loadCurrencyUseCase()
                _state.value = ExpensesIncomeViewModelState(
                    total = _convertAmountUseCase(_state.value.total, newCurrency),
                    expensesIncome = _state.value.expensesIncome.map { expenseIncome ->
                        expenseIncome.copy(
                            amount = _convertAmountUseCase(expenseIncome.amount, newCurrency)
                        )
                    }
                )
            }
        }
    }

    init {
        reloadData()
        observeReloadEvents()
    }

    /**
     * Фабрика по созданию вьюмодели экрана расходов и прокидывания в нее репозитория
     */
    class Factory(route: String) : ViewModelFactory<ExpensesIncomeViewModel>(
        viewModelClass = ExpensesIncomeViewModel::class.java,
        viewModelInit = {
            ExpensesIncomeViewModel(
                transactionsRepo = TransactionsRepoImpl(AccountRepoImpl.init()),
                isIncome = route == NavRoutes.Income.route
            )
        }
    )
}
