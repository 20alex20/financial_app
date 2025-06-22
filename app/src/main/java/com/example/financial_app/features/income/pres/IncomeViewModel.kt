package com.example.financial_app.features.income.pres

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.financial_app.common.code.getStringAmount
import com.example.financial_app.features.income.data.IncomeRepo
import com.example.financial_app.features.income.pres.models.IncomeUiModel
import kotlinx.coroutines.launch

class IncomeViewModel(application: Application) : AndroidViewModel(application) {
    private val incomeRepo = IncomeRepo(application)

    private val _totalSpent = mutableStateOf("0 ₽")
    val totalSpent: State<String> = _totalSpent

    private val _income = mutableStateOf(listOf<IncomeUiModel>())
    val income: State<List<IncomeUiModel>> = _income

    private fun loadTotalSpent() {
        viewModelScope.launch {
            try {
                val total = incomeRepo.getTotalSpent()
                _totalSpent.value = getStringAmount(total, incomeRepo.getCurrentCurrency())
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun loadIncome() {
        viewModelScope.launch {
            try {
                val currency = incomeRepo.getCurrentCurrency()
                val domainIncome = incomeRepo.getIncome()
                _income.value = domainIncome.map { income ->
                    IncomeUiModel(
                        id = income.id,
                        categoryName = income.categoryName,
                        categoryEmoji = income.categoryEmoji,
                        amount = getStringAmount(income.amount, currency),
                        comment = income.comment
                    )
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun refresh() {
        loadTotalSpent()
        loadIncome()
    }

    init {
        refresh()
    }
}
