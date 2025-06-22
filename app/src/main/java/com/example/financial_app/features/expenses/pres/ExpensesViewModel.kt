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

    private val _totalSpent = mutableStateOf("0 ₽")
    val totalSpent: State<String> = _totalSpent

    private val _expenses = mutableStateOf(listOf<ExpenseUiModel>())
    val expenses: State<List<ExpenseUiModel>> = _expenses

    private fun loadTotalSpent() {
        viewModelScope.launch {
            try {
                val total = expensesRepo.getTotalSpent()
                _totalSpent.value = getStringAmount(total, expensesRepo.getCurrentCurrency())
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun loadExpenses() {
        viewModelScope.launch {
            try {
                val currency = expensesRepo.getCurrentCurrency()
                val domainExpenses = expensesRepo.getExpenses()
                _expenses.value = domainExpenses.map { expense ->
                    ExpenseUiModel(
                        id = expense.id,
                        categoryName = expense.categoryName,
                        categoryEmoji = expense.categoryEmoji,
                        amount = getStringAmount(expense.amount, currency),
                        comment = expense.comment
                    )
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun refresh() {
        loadTotalSpent()
        loadExpenses()
    }

    init {
        refresh()
    }
}
