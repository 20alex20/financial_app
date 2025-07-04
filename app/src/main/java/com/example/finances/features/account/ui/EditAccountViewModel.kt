package com.example.finances.features.account.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.finances.core.data.Response
import com.example.finances.core.domain.ConvertAmountUseCase
import com.example.finances.core.domain.models.Currency
import com.example.finances.core.ui.viewmodel.BaseViewModel
import com.example.finances.core.ui.viewmodel.ViewModelFactory
import com.example.finances.features.account.data.AccountRepoImpl
import com.example.finances.features.account.domain.models.Account
import com.example.finances.features.account.ui.models.AccountViewModelState
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class EditAccountViewModel(private val accountRepo: AccountRepoImpl) : BaseViewModel() {
    private val _convertAmountUseCase = ConvertAmountUseCase()
    private var _deferred: Deferred<Boolean>? = null

    private var _account = Account(0, "Мой счет", 0.0, Currency.RUBLE)
    private var _isRealAccountId = false

    private val _state = mutableStateOf(AccountViewModelState("Мой счет", "0 ₽", "₽"))
    val state: State<AccountViewModelState> = _state

    override fun loadData() = viewModelScope.launch {
        try {
            when (val response = accountRepo.getAccount()) {
                is Response.Failure -> setError()
                is Response.Success -> {
                    resetLoadingAndError()
                    val account = response.data
                    _account = account.also { _isRealAccountId = true }
                    _state.value = AccountViewModelState(
                        accountName = account.name,
                        balance = _convertAmountUseCase(account.balance, account.currency),
                        currency = account.currency.symbol
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
        val amount = _convertAmountUseCase(newBalance)
        _account = _account.copy(balance = amount)
        _state.value = _state.value.copy(balance = _convertAmountUseCase(amount, _account.currency))
    }

    fun updateCurrency(newCurrency: Currency) {
        _account = _account.copy(currency = newCurrency)
        _state.value = _state.value.copy(currency = newCurrency.symbol)
    }

    fun saveChanges(): Deferred<Boolean> {
        _deferred?.cancel()
        return viewModelScope.async {
            setLoading()
            try {
                val response = accountRepo.updateAccount(_account, _isRealAccountId)
                resetLoadingAndError()
                return@async response is Response.Success && response.data == _account
            } catch (_: Exception) {
                resetLoadingAndError()
            }
            false
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
