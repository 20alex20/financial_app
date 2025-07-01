package com.example.finances.features.transactions.ui

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.finances.core.data.network.models.Response
import com.example.finances.core.data.repository.models.Currency
import com.example.finances.features.account.data.AccountRepoImpl
import com.example.finances.core.navigation.NavRoutes
import com.example.finances.features.transactions.data.TransactionsRepoImpl
import com.example.finances.features.transactions.ui.mappers.toExpenseIncome
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import com.example.finances.core.ui.viewmodel.BaseViewModel
import com.example.finances.core.ui.viewmodel.ViewModelFactory
import com.example.finances.features.transactions.ui.models.ExpensesIncomeViewModelState
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Вьюмодель экрана расходов/доходов
 */
class ExpensesIncomeViewModel private constructor(
    private val transactionsRepo: TransactionsRepo,
    private val isIncome: Boolean
) : BaseViewModel() {
    private val _state = mutableStateOf(ExpensesIncomeViewModelState("0 ₽", emptyList()))
    val state: State<ExpensesIncomeViewModelState> = _state

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
                        total = currency.getStrAmount(response.data.sumOf { it.amount }),
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
    class Factory(context: Context, route: String) : ViewModelFactory<ExpensesIncomeViewModel>(
        viewModelClass = ExpensesIncomeViewModel::class.java,
        viewModelInit = {
            ExpensesIncomeViewModel(
                transactionsRepo = TransactionsRepoImpl(context, AccountRepoImpl.init(context)),
                isIncome = route == NavRoutes.Income.route
            )
        }
    )
}
