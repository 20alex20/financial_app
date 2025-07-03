package com.example.finances.features.account.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.finances.core.data.network.models.Response
import com.example.finances.core.data.repository.mappers.toStrAmount
import com.example.finances.core.data.repository.models.Currency
import com.example.finances.core.ui.viewmodel.BaseViewModel
import com.example.finances.core.ui.viewmodel.ViewModelFactory
import com.example.finances.features.account.data.AccountRepoImpl
import com.example.finances.features.account.domain.mappers.toAmount
import com.example.finances.features.account.domain.mappers.toShortAccount
import com.example.finances.features.account.domain.models.ShortAccount
import com.example.finances.features.account.ui.models.AccountViewModelState
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class EditAccountViewModel(private val accountRepo: AccountRepoImpl) : BaseViewModel() {
    private var _account = ShortAccount("Мой счет", 0.0, Currency.RUBLE)
    private var _deferred: Deferred<Boolean>? = null

    private val _state = mutableStateOf(AccountViewModelState("Мой счет", "0 ₽", "₽"))
    val state: State<AccountViewModelState> = _state

    override fun loadData() = viewModelScope.launch {
        try {
            when (val response = accountRepo.getAccount()) {
                is Response.Failure -> setError()
                is Response.Success -> {
                    resetLoadingAndError()
                    _account = response.data.toShortAccount()
                    _state.value = AccountViewModelState(
                        accountName = response.data.name,
                        balance = response.data.balance.toStrAmount(response.data.currency),
                        currency = response.data.currency.symbol
                    )
                }
            }
        } catch (_: Exception) {
            setError()
        }
    }

    fun updateAccountName(newName: String) {
        _account = _account.copy(name = newName)
        _state.value = _state.value.copy(accountName = newName)
    }

    fun updateBalance(newBalance: String) {
        val amount = newBalance.toAmount()
        _account = _account.copy(balance = amount)
        _state.value = _state.value.copy(balance = amount.toStrAmount(_account.currency))
    }

    fun updateCurrency(newCurrency: Currency) {
        _account = _account.copy(currency = newCurrency)
        _state.value = _state.value.copy(currency = newCurrency.symbol)
    }

    fun saveChanges(): Deferred<Boolean> {
        _deferred?.cancel()
        return viewModelScope.async {
            setLoading()
            var success = false
            try {
                val response = accountRepo.updateAccount(_account)
                success = response is Response.Success && response.data.toShortAccount() == _account
            } catch (_: Exception) {
            }
            resetLoadingAndError()
            success
        }.also { _deferred = it }
    }

    init {
        reloadData()
    }

    class Factory : ViewModelFactory<EditAccountViewModel>(
        viewModelClass = EditAccountViewModel::class.java,
        viewModelInit = { EditAccountViewModel(AccountRepoImpl.init()) }
    )
}
