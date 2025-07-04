package com.example.finances.features.account.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.finances.core.data.Response
import com.example.finances.core.domain.ConvertAmountUseCase
import com.example.finances.features.account.data.AccountRepoImpl
import com.example.finances.features.account.domain.repository.AccountRepo
import com.example.finances.core.ui.viewmodel.BaseViewModel
import com.example.finances.core.ui.viewmodel.ViewModelFactory
import com.example.finances.features.account.ui.models.AccountViewModelState
import kotlinx.coroutines.launch

/**
 * Вьюмодель экрана счета
 */
class AccountViewModel private constructor(private val accountRepo: AccountRepo) : BaseViewModel() {
    private val _convertAmountUseCase = ConvertAmountUseCase()

    private val _state = mutableStateOf(AccountViewModelState("Мой счет", "0 ₽", "₽"))
    val state: State<AccountViewModelState> = _state

    override fun loadData() = viewModelScope.launch {
        try {
            when (val response = accountRepo.getAccount()) {
                is Response.Failure -> setError()
                is Response.Success -> {
                    resetLoadingAndError()
                    val account = response.data
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

    init {
        reloadData()
    }

    /**
     * Фабрика по созданию вьюмодели экрана счета и прокидывания в нее репозитория
     */
    class Factory : ViewModelFactory<AccountViewModel>(
        viewModelClass = AccountViewModel::class.java,
        viewModelInit = { AccountViewModel(AccountRepoImpl.init()) }
    )
}
