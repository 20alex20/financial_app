package com.example.finances.feature.account.api

import com.example.finances.feature.account.di.AccountComponent
import com.example.finances.feature.account.domain.repository.ExternalTransactionsRepo
import kotlinx.coroutines.flow.MutableStateFlow

object AccountComponentFactory {
    fun create(
        dependencies: AccountDependencies,
        externalTransactionsRepo: MutableStateFlow<ExternalTransactionsRepo?>
    ): AccountFeature {
        return AccountComponent.create(dependencies, externalTransactionsRepo).accountFeature()
    }
}
