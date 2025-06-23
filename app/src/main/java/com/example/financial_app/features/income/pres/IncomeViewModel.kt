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

    private val _total = mutableStateOf("0 ₽")
    val total: State<String> = _total

    private val _income = mutableStateOf(listOf<IncomeUiModel>())
    val income: State<List<IncomeUiModel>> = _income

    private fun loadIncomeAndTotal() {
        viewModelScope.launch {
            try {
                val currency = incomeRepo.getCurrency()
                val repoIncome = incomeRepo.getIncome()

                _income.value = repoIncome.map { income ->
                    IncomeUiModel(
                        id = income.id,
                        categoryName = income.categoryName,
                        categoryEmoji = income.categoryEmoji,
                        amount = getStringAmount(income.amount, currency),
                        comment = income.comment
                    )
                }

                _total.value = getStringAmount(repoIncome.sumOf { it.amount }, currency)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun refresh() {
        loadIncomeAndTotal()
    }

    init {
        refresh()
    }
}
