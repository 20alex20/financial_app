package com.example.financial_app.features.expenses.pres

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.financial_app.features.expenses.domain.models.Expense
import androidx.compose.runtime.State
import com.example.financial_app.features.expenses.data.ExpensesRepo

class ExpensesViewModel : ViewModel() {
    private val _balance = mutableStateOf("0 ₽")
    val balance: State<String> = _balance

    private val _expenses = mutableStateOf(listOf<Expense>())
    val expenses: State<List<Expense>> = _expenses

    private fun loadBalance() {
        _balance.value = ExpensesRepo.getBalance()
    }

    private fun loadExpenses() {
        _expenses.value = ExpensesRepo.getExpenses()
    }

    init {
        loadBalance()
        loadExpenses()
    }
}
