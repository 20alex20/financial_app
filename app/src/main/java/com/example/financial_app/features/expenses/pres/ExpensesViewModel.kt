package com.example.financial_app.features.expenses.pres

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.State
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.financial_app.R
import com.example.financial_app.common.code.getStringAmount
import com.example.financial_app.common.models.Response
import com.example.financial_app.common.usecase.ShowToastUseCase
import com.example.financial_app.features.expenses.data.ExpensesRepo
import com.example.financial_app.features.expenses.pres.models.ExpenseUiModel
import com.example.financial_app.features.network.data.models.Currency
import kotlinx.coroutines.launch

class ExpensesViewModel(application: Application) : AndroidViewModel(application) {
    private val expensesRepo = ExpensesRepo(application)
    private val showToast = ShowToastUseCase(application)

    private val _total = mutableStateOf("0 ₽")
    val total: State<String> = _total

    private val _expenses = mutableStateOf(listOf<ExpenseUiModel>())
    val expenses: State<List<ExpenseUiModel>> = _expenses

    private fun loadExpensesAndTotal() {
        viewModelScope.launch {
            try {
                val currencyResponse = expensesRepo.getCurrency()
                val expensesResponse = expensesRepo.getExpenses()
                if (expensesResponse is Response.Success) {
                    val currency = if (currencyResponse is Response.Success)
                        currencyResponse.data
                    else
                        Currency.RUBLE

                    _expenses.value = expensesResponse.data.map { expense ->
                        ExpenseUiModel(
                            id = expense.id,
                            categoryName = expense.categoryName,
                            categoryEmoji = expense.categoryEmoji,
                            amount = getStringAmount(expense.amount, currency),
                            comment = expense.comment
                        )
                    }

                    _total.value = getStringAmount(
                        expensesResponse.data.sumOf { it.amount },
                        currency
                    )
                } else {
                    showToast(application.getString(R.string.error_data_loading))
                }
            } catch (e: Exception) {
                showToast(application.getString(R.string.error_data_processing))
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
