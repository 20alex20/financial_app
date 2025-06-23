package com.example.financial_app.features.expenses.pres

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.financial_app.common.code.getStringAmount
import com.example.financial_app.features.expenses.data.ExpensesRepo
import com.example.financial_app.features.expenses.pres.models.ExpenseUiModel
import kotlinx.coroutines.launch

class ExpensesViewModel(application: Application) : AndroidViewModel(application) {
    private val expensesRepo = ExpensesRepo(application)

    private val _total = mutableStateOf("0 ₽")
    val total: State<String> = _total

    private val _expenses = mutableStateOf(listOf<ExpenseUiModel>())
    val expenses: State<List<ExpenseUiModel>> = _expenses

    private fun loadExpensesAndTotal() {
        viewModelScope.launch {
            try {
                val currency = expensesRepo.getCurrency()
                val repoExpenses = expensesRepo.getExpenses()

                _expenses.value = repoExpenses.map { expense ->
                    ExpenseUiModel(
                        id = expense.id,
                        categoryName = expense.categoryName,
                        categoryEmoji = expense.categoryEmoji,
                        amount = getStringAmount(expense.amount, currency),
                        comment = expense.comment
                    )
                }

                _total.value = getStringAmount(repoExpenses.sumOf { it.amount }, currency)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun refresh() {
        loadExpensesAndTotal()
    }

    init {
        refresh()
    }
}
