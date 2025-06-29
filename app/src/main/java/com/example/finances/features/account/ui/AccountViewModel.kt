package com.example.finances.features.account.ui

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.finances.core.data.network.models.Response
import com.example.finances.features.account.data.AccountRepoImpl
import com.example.finances.features.account.domain.repository.AccountRepo
import com.example.finances.core.ui.viewmodel.BaseViewModel
import com.example.finances.core.ui.viewmodel.ViewModelFactory
import com.example.finances.features.account.ui.models.AccountViewModelState
import kotlinx.coroutines.launch

/**
 * ViewModel экрана счета
 */
class AccountViewModel(private val accountRepo: AccountRepo) : BaseViewModel() {
    private val _state = mutableStateOf(AccountViewModelState("0 ₽", "₽"))
    val state: State<AccountViewModelState> = _state

    override fun loadData() = viewModelScope.launch {
        try {
            accountRepo.getAccount().collect { reply ->
                when (reply) {
                    is Response.Loading -> setLoading()
                    is Response.Success -> {
                        resetLoadingAndError()
                        _state.value = AccountViewModelState(
                            reply.data.currency.getStrAmount(reply.data.balance),
                            reply.data.currency.symbol
                        )
                    }
                    is Response.Failure -> setError()
                }
            }
        } catch (_: Exception) {
            setError()
        }
    }

    /**
     * Фабрика по созданию ViewModel экрана счета и прокидывания в нее репозитория
     */
    class Factory(context: Context) : ViewModelFactory<AccountViewModel>(
        viewModelClass = AccountViewModel::class.java,
        viewModelInit = { AccountViewModel(AccountRepoImpl.init(context)) }
    )
}
