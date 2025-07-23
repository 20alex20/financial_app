package com.example.finances.feature.account.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.finances.core.managers.ConvertAmountUseCase
import com.example.finances.core.utils.viewmodel.ReloadEvent
import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.models.Currency
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.feature.account.domain.models.ShortAccount
import com.example.finances.feature.account.domain.repository.AccountRepo
import com.example.finances.feature.account.ui.mappers.toShortAccount
import com.example.finances.feature.account.ui.models.AccountViewModelState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import javax.inject.Inject

/**
 * Вьюмодель экрана редактирования счета
 */
class EditAccountViewModel @Inject constructor(
    private val accountRepo: AccountRepo,
    private val convertAmountUseCase: ConvertAmountUseCase
) : BaseViewModel() {
    private var _deferredSaving: Deferred<Boolean>? = null

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
        val amount = convertAmountUseCase(newBalance)
        _account = _account.copy(balance = amount)
        _state.value = _state.value.copy(balance = convertAmountUseCase(amount, _account.currency))
    }

    fun updateCurrency(newCurrency: Currency) {
        _account = _account.copy(currency = newCurrency)
        _state.value = _state.value.copy(
            balance = convertAmountUseCase(_account.balance, newCurrency),
            currency = newCurrency.symbol
        )
    }

    override suspend fun loadData(scope: CoroutineScope) {
        when (val response = accountRepo.getAccount()) {
            is Response.Failure -> setError()
            is Response.Success -> {
                resetLoadingAndError()
                _accountId = response.data.id
                _account = response.data.toShortAccount()
                _state.value = AccountViewModelState(
                    accountName = response.data.name,
                    balance = convertAmountUseCase(response.data.balance, response.data.currency),
                    currency = response.data.currency.symbol
                )
            }
        }
    }

    fun saveChanges(): Deferred<Boolean> {
        _deferredSaving?.cancel()
        return viewModelScope.async {
            setLoading()
            try {
                val response = accountRepo.updateAccount(_account, _accountId)
                resetLoadingAndError()
                if (response is Response.Success && response.data.toShortAccount() == _account) {
                    send(ReloadEvent.AccountUpdated)
                    return@async true
                }
            } catch (_: Exception) {
                resetLoadingAndError()
            }
            false
        }.also { _deferredSaving = it }
    }

    override fun setViewModelParams(extras: CreationExtras) {}

    init {
        reloadData()
    }

    companion object {
        private const val MAX_ACCOUNT_NAME_LENGTH = 32
    }
}
