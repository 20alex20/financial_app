package com.example.finances.core.managers

import android.content.Context
import com.example.finances.core.di.ActivityContext
import com.example.finances.core.di.ActivityScope
import com.example.finances.features.account.data.mappers.toShortAccount
import com.example.finances.features.account.domain.repository.AccountRepo
import com.example.finances.features.transactions.data.mappers.toShortTransaction
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import com.example.finances.features.transactions.navigation.ScreenType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScope
class DataSyncOnConnection @Inject constructor(
    @ActivityContext private val context: Context,
    private val networkObserver: NetworkConnectionObserver,
    private val accountRepo: AccountRepo,
    private val transactionsRepo: TransactionsRepo,
    private val database: FinanceDatabase
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        observeNetworkState()
    }

    private fun observeNetworkState() = scope.launch {
        networkObserver.observe().distinctUntilChanged().collect { isOnline ->
            if (isOnline) {
                syncAccount()
                if (syncTransactions())
                    SyncTimeManager.updateLastSyncTime(context.applicationContext)
            }
        }
    }

    private suspend fun syncAccount() {
        val localAccount = database.accountDao().getAccount()
        if (localAccount != null && !localAccount.isSynced) {
            accountRepo.updateAccount(
                account = localAccount.toShortAccount(),
                accountId = localAccount.id
            )
        }
    }

    private suspend fun syncTransactions(): Boolean {
        var anySynced = false
        val localTransactions = database.transactionDao().getNotSyncedTransactions()
        for (transaction in localTransactions) {
            transactionsRepo.createUpdateTransaction(
                transaction = transaction.toShortTransaction(),
                transactionId = transaction.id.toInt(),
                screenType = ScreenType.fromBoolean(transaction.isIncome)
            )
            anySynced = true
        }
        return anySynced
    }
}
