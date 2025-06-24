package com.example.financial_app.features.expenses.pres

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.State
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.financial_app.R
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

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private fun loadExpensesAndTotal() {
        viewModelScope.launch {
            try {
                val currencyResponse = expensesRepo.getCurrency()
                val currency = if (currencyResponse is Response.Success)
                    currencyResponse.data
                else
                    Currency.RUBLE

                expensesRepo.getExpenses().collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true

                        is Response.Success -> {
                            _expenses.value = response.data.map { expense ->
                                ExpenseUiModel(
                                    id = expense.id,
                                    categoryName = expense.categoryName,
                                    categoryEmoji = expense.categoryEmoji,
                                    amount = currency.getStrAmount(expense.amount),
                                    comment = expense.comment
                                )
                            }
                            _total.value = currency.getStrAmount(response.data.sumOf { expense ->
                                expense.amount
                            })
                        }

                        is Response.Failure -> {
                            showToast(application.getString(R.string.error_data_loading))
                        }
                    }
                }
            } catch (e: Exception) {
                // showToast(application.getString(R.string.error_data_processing))
            }
        }
        _loading.value = false
    }

    fun refresh() {
        loadExpensesAndTotal()
    }

    init {
        refresh()
    }
}
