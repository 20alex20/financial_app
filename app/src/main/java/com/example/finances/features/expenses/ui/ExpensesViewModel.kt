package com.example.finances.features.expenses.ui

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.finances.common.models.Response
import com.example.finances.features.expenses.ui.models.ExpenseUiModel
import com.example.finances.common.models.Currency
import com.example.finances.features.account.data.AccountRepoImpl
import com.example.finances.features.expenses.data.ExpensesRepoImpl
import com.example.finances.features.expenses.domain.models.Expense
import com.example.finances.features.expenses.domain.repo.ExpensesRepo
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch

class ExpensesViewModel(private val expensesRepo: ExpensesRepo) : ViewModel() {
    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _error = mutableStateOf(false)
    val error: State<Boolean> = _error

    private val _expenses = mutableStateOf(listOf<ExpenseUiModel>())
    val expenses: State<List<ExpenseUiModel>> = _expenses

    private val _total = mutableStateOf("0 ₽")
    val total: State<String> = _total

    private suspend fun getCurrency(): Currency {
        val currencyResponse = expensesRepo.getCurrency().lastOrNull()
        return if (currencyResponse is Response.Success)
            currencyResponse.data
        else
            Currency.RUBLE
    }

    private fun convertExpense(expense: Expense, currency: Currency) = ExpenseUiModel(
        id = expense.id,
        categoryName = expense.categoryName,
        categoryEmoji = expense.categoryEmoji,
        amount = currency.getStrAmount(expense.amount),
        comment = expense.comment
    )

    private fun showError() {
        _loading.value = false
        _error.value = true
    }

    private fun loadExpensesAndTotal() = viewModelScope.launch {
        try {
            _error.value = false
            val currency = getCurrency()
            expensesRepo.getExpenses().collect { response ->
                when (response) {
                    is Response.Loading -> _loading.value = true
                    is Response.Success -> {
                        _loading.value = false
                        _error.value = false
                        _expenses.value = response.data.map { convertExpense(it, currency) }
                        _total.value = currency.getStrAmount(response.data.sumOf { it.amount })
                    }
                    is Response.Failure -> showError()
                }
            }
        } catch (_: Exception) {
            showError()
        }
    }

    init {
        loadExpensesAndTotal()
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExpensesViewModel::class.java))
                return ExpensesViewModel(
                    expensesRepo = ExpensesRepoImpl(context, AccountRepoImpl.init(context))
                ) as T
            throw IllegalArgumentException(UNKNOWN_VIEWMODEL)
        }
    }

    companion object {
        private const val UNKNOWN_VIEWMODEL = "Unknown ViewModel class"
    }
}
