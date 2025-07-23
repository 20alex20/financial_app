package com.example.finances.feature.account.di

import com.example.finances.feature.account.api.AccountDependencies
import com.example.finances.feature.account.api.AccountFeature
import com.example.finances.feature.account.di.modules.AccountModule
import dagger.Component

@AccountScope
@Component(
    dependencies = [AccountDependencies::class],
    modules = [AccountModule::class]
)
interface AccountComponent {
    fun accountFeature(): AccountFeature

    @Component.Factory
    interface Factory {
        fun create(dependencies: AccountDependencies): AccountComponent
    }

    companion object {
        fun create(dependencies: AccountDependencies): AccountComponent {
            return DaggerAccountComponent.factory().create(dependencies)
        }
    }
}
