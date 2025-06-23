package com.example.financial_app.features.income.pres

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.financial_app.R
import com.example.financial_app.common.code.getStringAmount
import com.example.financial_app.common.models.Response
import com.example.financial_app.common.usecase.ShowToastUseCase
import com.example.financial_app.features.income.data.IncomeRepo
import com.example.financial_app.features.income.pres.models.IncomeUiModel
import com.example.financial_app.features.network.data.models.Currency
import kotlinx.coroutines.launch

class IncomeViewModel(application: Application) : AndroidViewModel(application) {
    private val incomeRepo = IncomeRepo(application)
    private val showToast = ShowToastUseCase(application)

    private val _total = mutableStateOf("0 ₽")
    val total: State<String> = _total

    private val _income = mutableStateOf(listOf<IncomeUiModel>())
    val income: State<List<IncomeUiModel>> = _income

    private fun loadIncomeAndTotal() {
        viewModelScope.launch {
            try {
                val currencyResponse = incomeRepo.getCurrency()
                val incomeResponse = incomeRepo.getIncome()
                if (incomeResponse is Response.Success) {
                    val currency = if (currencyResponse is Response.Success)
                        currencyResponse.data
                    else
                        Currency.RUBLE

                    _income.value = incomeResponse.data.map { income ->
                        IncomeUiModel(
                            id = income.id,
                            categoryName = income.categoryName,
                            categoryEmoji = income.categoryEmoji,
                            amount = getStringAmount(income.amount, currency),
                            comment = income.comment
                        )
                    }

                    _total.value = getStringAmount(
                        incomeResponse.data.sumOf { it.amount },
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
        loadIncomeAndTotal()
    }

    init {
        refresh()
    }
}
