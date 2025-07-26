package com.example.finances.feature.transactions.di

import com.example.finances.feature.transactions.api.TransactionsDependencies
import com.example.finances.feature.transactions.api.TransactionsFeature
import com.example.finances.feature.transactions.di.modules.TransactionsModule
import dagger.Component

@TransactionsScope
@Component(
    dependencies = [TransactionsDependencies::class],
    modules = [TransactionsModule::class]
)
interface TransactionsComponent {
    fun transactionsFeature(): TransactionsFeature

    @Component.Factory
    interface Factory {
        fun create(dependencies: TransactionsDependencies): TransactionsComponent
    }

    companion object {
        fun create(dependencies: TransactionsDependencies): TransactionsComponent {
            return DaggerTransactionsComponent.factory().create(dependencies)
        }
    }
}
