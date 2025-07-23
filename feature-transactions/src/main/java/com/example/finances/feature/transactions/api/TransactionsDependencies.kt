package com.example.finances.feature.transactions.api

import com.example.finances.core.managers.ConvertAmountUseCase
import com.example.finances.core.managers.NetworkConnectionObserver
import com.example.finances.feature.account.domain.repository.ExternalAccountRepo
import com.example.finances.feature.categories.domain.repository.CategoriesRepo
import retrofit2.Retrofit

interface TransactionsDependencies {
    fun retrofit(): Retrofit

    fun accountRepo(): ExternalAccountRepo

    fun categoriesRepo(): CategoriesRepo

    fun transactionsDatabase(): TransactionsDatabase

    fun networkConnectionObserver(): NetworkConnectionObserver

    fun convertAmountUseCase(): ConvertAmountUseCase
}
