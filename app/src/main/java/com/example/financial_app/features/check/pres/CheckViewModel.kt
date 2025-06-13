package com.example.financial_app.features.check.pres

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import com.example.financial_app.features.check.data.CheckRepo

class CheckViewModel : ViewModel() {
    private val _balance = mutableStateOf("0 ₽")
    val balance: State<String> = _balance

    private val _currency = mutableStateOf("₽")
    val currency: State<String> = _currency

    fun loadBalance() {
        _balance.value = CheckRepo.getBalance()
    }

    fun loadCurrency() {
        _currency.value = CheckRepo.getCurrency()
    }

    init {
        loadBalance()
        loadCurrency()
    }
}
