package com.example.finances.features.transactions.ui

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.finances.core.data.network.models.Response
import com.example.finances.core.data.repository.models.Currency
import com.example.finances.features.account.data.AccountRepoImpl
import com.example.finances.core.navigation.NavRoutes
import com.example.finances.features.transactions.data.TransactionsRepoImpl
import com.example.finances.features.transactions.domain.mappers.toUiTransaction
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import com.example.finances.features.transactions.ui.models.UiTransaction
import com.example.finances.core.ui.viewmodel.BaseViewModel
import com.example.finances.core.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch

/**
 * ViewModel экрана расходов
 */
class TransactionsViewModel(
    private val transactionsRepo: TransactionsRepo,
    private val isIncome: Boolean
) : BaseViewModel() {
    private val _transactions = mutableStateOf(listOf<UiTransaction>())
    val transactions: State<List<UiTransaction>> = _transactions

    private val _total = mutableStateOf("0 ₽")
    val total: State<String> = _total

    override fun loadData() = viewModelScope.launch {
        try {
            resetLoadingAndError()
            val currency = transactionsRepo.getCurrency().lastOrNull()
                .let { if (it is Response.Success) it.data else Currency.RUBLE }
            transactionsRepo.getTransactions(isIncome).collect { response ->
                when (response) {
                    is Response.Loading -> setLoading()
                    is Response.Success -> {
                        resetLoadingAndError()
                        _transactions.value = response.data.map { it.toUiTransaction(currency) }
                        _total.value = currency.getStrAmount(response.data.sumOf { it.amount })
                    }
                    is Response.Failure -> setError()
                }
            }
        } catch (_: Exception) {
            setError()
        }
    }

    /**
     * Фабрика по созданию ViewModel экрана расходов и прокидывания в нее репозитория
     */
    class Factory(context: Context, route: String) : ViewModelFactory<TransactionsViewModel>(
        viewModelClass = TransactionsViewModel::class.java,
        viewModelInit = {
            TransactionsViewModel(
                transactionsRepo = TransactionsRepoImpl(context, AccountRepoImpl.init(context)),
                isIncome = route == NavRoutes.Income.route
            )
        }
    )
}
