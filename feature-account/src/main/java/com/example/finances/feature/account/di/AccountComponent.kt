package com.example.finances.feature.account.di

import com.example.finances.core.di.ActivityScope
import com.example.finances.core.di.FeatureComponent
import com.example.finances.feature.account.domain.repository.AccountRepository
import com.example.finances.feature.account.ui.AccountViewModel
import dagger.Component

@ActivityScope
@Component(
    dependencies = [AccountDependencies::class],
    modules = [AccountModule::class]
)
interface AccountComponent : FeatureComponent {
    fun accountViewModel(): AccountViewModel
    fun accountRepository(): AccountRepository

    @Component.Factory
    interface Factory : FeatureComponent.Factory<AccountComponent> {
        override fun create(dependencies: AccountDependencies): AccountComponent
    }
} 