package com.example.finances.features.account.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.finances.core.utils.viewmodel.ReloadEvent
import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.usecases.ConvertAmountUseCase
import com.example.finances.features.account.domain.repository.AccountRepo
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.features.account.ui.models.AccountViewModelState
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**
 * Вьюмодель экрана счета
 */
class AccountViewModel @Inject constructor(
    private val accountRepo: AccountRepo,
    private val convertAmountUseCase: ConvertAmountUseCase
) : BaseViewModel() {
    private val _state = mutableStateOf(AccountViewModelState("Мой счет", "0 ₽", "₽"))
    val state: State<AccountViewModelState> = _state

    override suspend fun loadData(scope: CoroutineScope) {
        when (val response = accountRepo.getAccount()) {
            is Response.Failure -> setError()
            is Response.Success -> {
                resetLoadingAndError()
                _state.value = AccountViewModelState(
                    accountName = response.data.name,
                    balance = convertAmountUseCase(response.data.balance, response.data.currency),
                    currency = response.data.currency.symbol
                )
            }
        }
    }

    override suspend fun handleReloadEvent(reloadEvent: ReloadEvent) {
        when (reloadEvent) {
            ReloadEvent.AccountUpdated -> reloadData()
            ReloadEvent.TransactionCreatedUpdated -> reloadData()
        }
    }

    override fun setViewModelParams(extras: CreationExtras) {}

    init {
        reloadData()
        observeReloadEvents()
    }
}
