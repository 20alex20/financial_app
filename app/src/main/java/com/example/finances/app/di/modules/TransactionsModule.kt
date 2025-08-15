package com.example.finances.app.di.modules

import com.example.finances.app.di.ActivityComponent
import com.example.finances.app.di.ActivityScope
import com.example.finances.app.di.TransactionsNavigation
import com.example.finances.app.managers.FinanceDatabase
import com.example.finances.core.navigation.FeatureNavigation
import com.example.finances.core.utils.viewmodel.ViewModelMapProvider
import com.example.finances.feature.account.domain.repository.ExternalTransactionsRepo
import com.example.finances.feature.transactions.api.TransactionsComponentFactory
import com.example.finances.feature.transactions.api.TransactionsDatabase
import com.example.finances.feature.transactions.api.TransactionsDependencies
import com.example.finances.feature.transactions.api.TransactionsFeature
import com.example.finances.feature.transactions.domain.repository.TransactionsRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import kotlinx.coroutines.flow.MutableStateFlow

@Module
interface TransactionsModule {
    @Binds
    fun bindsTransactionsDependencies(dependencies: ActivityComponent): TransactionsDependencies

    @Binds
    fun bindsTransactionsDatabase(financeDatabase: FinanceDatabase): TransactionsDatabase

    @Binds
    @TransactionsNavigation
    fun bindsTransactionsNavigation(transactionsFeature: TransactionsFeature): FeatureNavigation

    companion object {
        @Provides
        @ActivityScope
        fun providesTransactionsFeature(
            dependencies: TransactionsDependencies,
            externalTransactionsRepo: MutableStateFlow<ExternalTransactionsRepo?>
        ): TransactionsFeature {
            return TransactionsComponentFactory.create(dependencies).apply {
                externalTransactionsRepo.value = getExternalTransactionsRepo()
            }
        }

        @Provides
        @IntoSet
        fun providesTransactionsViewModelProviders(
            transactionsFeature: TransactionsFeature
        ): ViewModelMapProvider {
            return transactionsFeature.getViewModelMapProvider()
        }

        @Provides
        fun providesTransactionsRepo(transactionsFeature: TransactionsFeature): TransactionsRepo {
            return transactionsFeature.getTransactionsRepo()
        }
    }
}
