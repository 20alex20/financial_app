package com.example.finances.feature.transactions.di

import com.example.finances.core.di.ActivityComponent
import com.example.finances.feature.account.domain.repository.AccountRepository
import retrofit2.Retrofit

interface TransactionsDependencies : ActivityComponent {
    fun retrofit(): Retrofit
    fun accountRepository(): AccountRepository
} 