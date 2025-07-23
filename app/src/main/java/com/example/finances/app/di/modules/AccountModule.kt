package com.example.finances.app.di.modules

import com.example.finances.app.di.ActivityComponent
import com.example.finances.app.di.ActivityScope
import com.example.finances.app.managers.FinanceDatabase
import com.example.finances.core.utils.viewmodel.ViewModelMapProvider
import com.example.finances.feature.account.api.AccountComponentFactory
import com.example.finances.feature.account.api.AccountDatabase
import com.example.finances.feature.account.api.AccountDependencies
import com.example.finances.feature.account.api.AccountFeature
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
interface AccountModule {
    @Binds
    fun bindsAccountDependencies(dependencies: ActivityComponent): AccountDependencies

    @Binds
    fun bindsAccountDatabase(financeDatabase: FinanceDatabase): AccountDatabase

    companion object {
        @Provides
        @ActivityScope
        fun providesAccountFeature(
            dependencies: AccountDependencies
        ): AccountFeature {
            return AccountComponentFactory.create(dependencies)
        }

        @Provides
        @IntoSet
        fun providesAccountViewModelProviders(
            accountFeature: AccountFeature
        ): ViewModelMapProvider {
            return accountFeature.getViewModelMapProvider()
        }
    }
}
