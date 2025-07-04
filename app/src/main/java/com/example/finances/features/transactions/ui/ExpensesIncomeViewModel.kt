package com.example.finances.features.transactions.ui

import androidx.lifecycle.viewModelScope
import com.example.finances.core.data.Response
import com.example.finances.core.domain.ConvertAmountUseCase
import com.example.finances.core.domain.models.Currency
import com.example.finances.features.account.data.AccountRepoImpl
import com.example.finances.core.navigation.NavRoutes
import com.example.finances.features.transactions.data.TransactionsRepoImpl
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import com.example.finances.core.ui.viewmodel.BaseViewModel
import com.example.finances.core.ui.viewmodel.ViewModelFactory
import com.example.finances.features.transactions.ui.mappers.toExpenseIncome
import com.example.finances.features.transactions.ui.models.ExpensesIncomeViewModelState
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Вьюмодель экрана расходов/доходов
 */
class ExpensesIncomeViewModel private constructor(
    private val transactionsRepo: TransactionsRepo,
    private val isIncome: Boolean
) : BaseViewModel() {
    private val _convertAmountUseCase = ConvertAmountUseCase()

    private val _state = MutableStateFlow(ExpensesIncomeViewModelState("0 ₽", emptyList()))
    val state: StateFlow<ExpensesIncomeViewModelState> = _state.asStateFlow()

    override fun loadData() = viewModelScope.launch {
        try {
            val currency = transactionsRepo.getCurrency().let {
                if (it is Response.Success) it.data else Currency.RUBLE
            }
            val today = LocalDate.now()
            when (val response = transactionsRepo.getTransactions(today, today, isIncome)) {
                is Response.Failure -> setError()
                is Response.Success -> {
                    resetLoadingAndError()
                    _state.value = ExpensesIncomeViewModelState(
                        total = _convertAmountUseCase(response.data.sumOf { it.amount }, currency),
                        expensesIncome = response.data.map { it.toExpenseIncome(currency) }
                    )
                }
            }
        } catch (_: Exception) {
            setError()
        }
    }

    init {
        reloadData()
    }

    /**
     * Фабрика по созданию ViewModel экрана расходов и прокидывания в нее репозитория
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
