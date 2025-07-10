package com.example.finances.feature.transactions.di

import com.example.finances.core.di.ActivityScope
import com.example.finances.core.di.FeatureComponent
import com.example.finances.feature.transactions.domain.repository.TransactionsRepository
import com.example.finances.feature.transactions.ui.ExpensesIncomeViewModel
import com.example.finances.feature.transactions.ui.HistoryViewModel
import dagger.Component

@ActivityScope
@Component(
    dependencies = [TransactionsDependencies::class],
    modules = [TransactionsModule::class]
)
interface TransactionsComponent : FeatureComponent {
    fun expensesIncomeViewModel(): ExpensesIncomeViewModel
    fun historyViewModel(): HistoryViewModel
    fun transactionsRepository(): TransactionsRepository

    @Component.Factory
    interface Factory : FeatureComponent.Factory<TransactionsComponent> {
        override fun create(dependencies: TransactionsDependencies): TransactionsComponent
    }
} 