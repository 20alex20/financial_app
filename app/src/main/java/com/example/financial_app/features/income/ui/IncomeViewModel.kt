package com.example.financial_app.features.income.ui

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.financial_app.common.models.Response
import com.example.financial_app.features.income.data.IncomeRepoImpl
import com.example.financial_app.features.income.ui.models.IncomeUiModel
import com.example.financial_app.common.models.Currency
import com.example.financial_app.features.account.data.AccountRepoImpl
import com.example.financial_app.features.income.domain.models.Income
import com.example.financial_app.features.income.domain.repo.IncomeRepo
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch

class IncomeViewModel(private val incomeRepo: IncomeRepo) : ViewModel() {
    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _error = mutableStateOf(false)
    val error: State<Boolean> = _error

    private val _income = mutableStateOf(listOf<IncomeUiModel>())
    val income: State<List<IncomeUiModel>> = _income

    private val _total = mutableStateOf("0 ₽")
    val total: State<String> = _total

    private suspend fun getCurrency(): Currency {
        val currencyResponse = incomeRepo.getCurrency().lastOrNull()
        return if (currencyResponse is Response.Success)
            currencyResponse.data
        else
            Currency.RUBLE
    }

    private fun convertIncome(income: Income, currency: Currency) = IncomeUiModel(
        id = income.id,
        categoryName = income.categoryName,
        categoryEmoji = income.categoryEmoji,
        amount = currency.getStrAmount(income.amount),
        comment = income.comment
    )

    private fun showError() {
        _loading.value = false
        _error.value = true
    }

    private fun loadIncomeAndTotal() = viewModelScope.launch {
        try {
            _error.value = false
            val currency = getCurrency()
            incomeRepo.getIncome().collect { response ->
                when (response) {
                    is Response.Loading -> _loading.value = true
                    is Response.Success -> {
                        _loading.value = false
                        _error.value = false
                        _income.value = response.data.map { convertIncome(it, currency) }
                        _total.value = currency.getStrAmount(response.data.sumOf { it.amount })
                    }
                    is Response.Failure -> showError()
                }
            }
        } catch (e: Exception) {
            showError()
        }
    }

    init {
        loadIncomeAndTotal()
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(IncomeViewModel::class.java))
                return IncomeViewModel(
                    incomeRepo = IncomeRepoImpl(context, AccountRepoImpl.init(context))
                ) as T
            throw Exception(UNKNOWN_VIEWMODEL)
        }
    }

    companion object {
        private const val UNKNOWN_VIEWMODEL = "Unknown ViewModel class"
    }
}
