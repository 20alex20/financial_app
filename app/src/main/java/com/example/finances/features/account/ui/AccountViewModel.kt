package com.example.finances.features.account.ui

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.finances.common.models.Response
import com.example.finances.features.account.data.AccountRepoImpl
import com.example.finances.features.account.domain.repo.AccountRepo
import kotlinx.coroutines.launch

/**
 * ViewModel экрана счета
 */
class AccountViewModel(private val accountRepo: AccountRepo) : ViewModel() {
    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _error = mutableStateOf(false)
    val error: State<Boolean> = _error

    private val _balance = mutableStateOf("0 ₽")
    val balance: State<String> = _balance

    private val _currency = mutableStateOf("₽")
    val currency: State<String> = _currency

    private fun showError() {
        _loading.value = false
        _error.value = true
    }

    private fun loadBalanceAndCurrency() = viewModelScope.launch {
        try {
            _error.value = false
            accountRepo.getAccount().collect { response ->
                when (response) {
                    is Response.Loading -> _loading.value = true
                    is Response.Success -> {
                        _loading.value = false
                        _error.value = false
                        _balance.value = response.data.currency.getStrAmount(response.data.balance)
                        _currency.value = response.data.currency.symbol
                    }
                    is Response.Failure -> showError()
                }
            }
        } catch (_: Exception) {
            showError()
        }
    }

    init {
        loadBalanceAndCurrency()
    }

    /**
     * Фабрика по созданию ViewModel экрана счета и прокидывания в нее репозитория
     */
    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AccountViewModel::class.java))
                return AccountViewModel(AccountRepoImpl.init(context)) as T
            throw IllegalArgumentException(UNKNOWN_VIEWMODEL)
        }
    }

    companion object {
        private const val UNKNOWN_VIEWMODEL = "Unknown ViewModel class"
    }
}
