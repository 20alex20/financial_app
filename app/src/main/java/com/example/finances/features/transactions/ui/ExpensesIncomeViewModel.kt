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
import com.example.finances.features.transactions.ui.models.ExpenseIncome
import com.example.finances.core.ui.viewmodel.BaseViewModel
import com.example.finances.core.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * ViewModel экрана расходов
 */
class ExpensesIncomeViewModel(
    private val transactionsRepo: TransactionsRepo,
    private val isIncome: Boolean
) : BaseViewModel() {
    private val _expensesIncome = mutableStateOf(listOf<ExpenseIncome>())
    val expensesIncome: State<List<ExpenseIncome>> = _expensesIncome

    private val _total = mutableStateOf("0 ₽")
    val total: State<String> = _total

    override fun loadData() = viewModelScope.launch {
        try {
            val currency = transactionsRepo.getCurrency().lastOrNull()
                .let { if (it is Response.Success) it.data else Currency.RUBLE }
            val today = LocalDate.now()
            transactionsRepo.getTransactions(today, today, isIncome).collect { reply ->
                when (reply) {
                    is Response.Loading -> setLoading()
                    is Response.Failure -> setError()
                    is Response.Success -> {
                        resetLoadingAndError()
                        _expensesIncome.value = reply.data.map { it.toExpenseIncome(currency) }
                        _total.value = currency.getStrAmount(reply.data.sumOf { it.amount })
                    }
                }
            }
        } catch (_: Exception) {
            setError()
        }
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
