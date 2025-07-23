package com.example.finances.feature.transactions.api

import com.example.finances.feature.transactions.di.TransactionsComponent

object TransactionsComponentFactory {
    fun create(dependencies: TransactionsDependencies): TransactionsFeature {
        return TransactionsComponent.create(dependencies).transactionsFeature()
    }
}
