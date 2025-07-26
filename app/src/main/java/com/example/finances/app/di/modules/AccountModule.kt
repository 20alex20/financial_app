package com.example.finances.app.di.modules

import com.example.finances.app.di.AccountNavigation
import com.example.finances.app.di.ActivityComponent
import com.example.finances.app.di.ActivityScope
import com.example.finances.app.managers.FinanceDatabase
import com.example.finances.core.navigation.FeatureNavigation
import com.example.finances.core.utils.viewmodel.ViewModelMapProvider
import com.example.finances.feature.account.api.AccountComponentFactory
import com.example.finances.feature.account.api.AccountDatabase
import com.example.finances.feature.account.api.AccountDependencies
import com.example.finances.feature.account.api.AccountFeature
import com.example.finances.feature.account.domain.repository.ExternalAccountRepo
import com.example.finances.feature.account.domain.repository.ExternalTransactionsRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import kotlinx.coroutines.flow.MutableStateFlow

@Module
interface AccountModule {
    @Binds
    fun bindsAccountDependencies(dependencies: ActivityComponent): AccountDependencies

    @Binds
    fun bindsAccountDatabase(financeDatabase: FinanceDatabase): AccountDatabase

    @Binds
    @AccountNavigation
    fun bindsAccountNavigation(accountFeature: AccountFeature): FeatureNavigation

    companion object {
        @Provides
        @ActivityScope
        fun providesAccountFeature(
            dependencies: AccountDependencies,
            externalTransactionsRepo: MutableStateFlow<ExternalTransactionsRepo?>
        ): AccountFeature {
            return AccountComponentFactory.create(dependencies, externalTransactionsRepo)
        }

        @Provides
        @IntoSet
        fun providesAccountViewModelProviders(
            accountFeature: AccountFeature
        ): ViewModelMapProvider {
            return accountFeature.getViewModelMapProvider()
        }

        @Provides
        fun providesExternalAccountRepo(accountFeature: AccountFeature): ExternalAccountRepo {
            return accountFeature.getExternalAccountRepo()
        }
    }
}
