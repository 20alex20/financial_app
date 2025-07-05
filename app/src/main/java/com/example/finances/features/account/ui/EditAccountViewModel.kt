package com.example.finances.features.account.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.finances.core.buses.ReloadEvent
import com.example.finances.core.data.Response
import com.example.finances.core.domain.ConvertAmountUseCase
import com.example.finances.core.domain.models.Currency
import com.example.finances.core.ui.viewmodel.BaseViewModel
import com.example.finances.core.ui.viewmodel.ViewModelFactory
import com.example.finances.features.account.data.AccountRepoImpl
import com.example.finances.features.account.domain.models.ShortAccount
import com.example.finances.features.account.ui.mappers.toShortAccount
import com.example.finances.features.account.ui.models.AccountViewModelState
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

/**
 * Вьюмодель экрана редактирования счета
 */
class EditAccountViewModel(private val accountRepo: AccountRepoImpl) : BaseViewModel() {
    private val _convertAmountUseCase = ConvertAmountUseCase()
    private var _deferred: Deferred<Boolean>? = null

    private var _account = ShortAccount("Мой счет", 0.0, Currency.RUBLE)
    private var _accountId: Int? = null

    private val _state = mutableStateOf(AccountViewModelState("Мой счет", "0 ₽", "₽"))
    val state: State<AccountViewModelState> = _state

    fun updateAccountName(newName: String) {
        val accountName = newName.take(MAX_ACCOUNT_NAME_LENGTH)
        _account = _account.copy(name = accountName)
        _state.value = _state.value.copy(accountName = accountName)
    }

    fun updateBalance(newBalance: String) {
        val amount = _convertAmountUseCase(newBalance)
        _account = _account.copy(balance = amount)
        _state.value = _state.value.copy(balance = _convertAmountUseCase(amount, _account.currency))
    }

    fun updateCurrency(newCurrency: Currency) {
        _account = _account.copy(currency = newCurrency)
        _state.value = _state.value.copy(
            balance = _convertAmountUseCase(_account.balance, newCurrency),
            currency = newCurrency.symbol
        )
    }

    override suspend fun loadData() {
        when (val response = accountRepo.getAccount()) {
            is Response.Failure -> setError()
            is Response.Success -> {
                resetLoadingAndError()
                _accountId = response.data.id
                _account = response.data.toShortAccount()
                _state.value = AccountViewModelState(
                    accountName = response.data.name,
                    balance = _convertAmountUseCase(response.data.balance, response.data.currency),
                    currency = response.data.currency.symbol
                )
            }
        }
    }

    fun saveChanges(): Deferred<Boolean> {
        _deferred?.cancel()
        return viewModelScope.async {
            setLoading()
            try {
                val response = accountRepo.updateAccount(_account, _accountId)
                resetLoadingAndError()
                if (response is Response.Success && response.data.toShortAccount() == _account) {
                    sendReloadEvent(ReloadEvent.AccountUpdated)
                    return@async true
                }
            } catch (_: Exception) {
                resetLoadingAndError()
            }
            false
        }.also { _deferred = it }
    }

    init {
        reloadData()
    }

    /**
     * Фабрика по созданию вьюмодели экрана редактирования счета и прокидывания в нее репозитория
     */
    class Factory : ViewModelFactory<EditAccountViewModel>(
        viewModelClass = EditAccountViewModel::class.java,
        viewModelInit = { EditAccountViewModel(AccountRepoImpl.init()) }
    )

    companion object {
        private const val MAX_ACCOUNT_NAME_LENGTH = 32
    }
}
