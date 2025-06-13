package com.example.financial_app.features.income.pres

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.financial_app.features.income.domain.models.Income
import androidx.compose.runtime.State
import com.example.financial_app.features.income.data.IncomeRepo

class IncomeViewModel : ViewModel() {
    private val _balance = mutableStateOf("0 ₽")
    val balance: State<String> = _balance

    private val _income = mutableStateOf(listOf<Income>())
    val income: State<List<Income>> = _income

    fun loadBalance() {
        _balance.value = IncomeRepo.getBalance()
    }

    fun loadincome() {
        _income.value = IncomeRepo.getIncome()
    }

    init {
        loadBalance()
        loadincome()
    }
}
