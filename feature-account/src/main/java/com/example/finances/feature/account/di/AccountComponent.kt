package com.example.finances.feature.account.di

import com.example.finances.feature.account.api.AccountDependencies
import com.example.finances.feature.account.api.AccountFeature
import com.example.finances.feature.account.di.modules.AccountModule
import com.example.finances.feature.account.domain.repository.ExternalTransactionsRepo
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.flow.MutableStateFlow

@AccountScope
@Component(
    dependencies = [AccountDependencies::class],
    modules = [AccountModule::class]
)
interface AccountComponent {
    fun accountFeature(): AccountFeature

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance externalTransactionsRepo: MutableStateFlow<ExternalTransactionsRepo?>,
            dependencies: AccountDependencies
        ): AccountComponent
    }

    companion object {
        fun create(
            dependencies: AccountDependencies,
            externalTransactionsRepo: MutableStateFlow<ExternalTransactionsRepo?>
        ): AccountComponent {
            return DaggerAccountComponent.factory().create(externalTransactionsRepo, dependencies)
        }
    }
}
