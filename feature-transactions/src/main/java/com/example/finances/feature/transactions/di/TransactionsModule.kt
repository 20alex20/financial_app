package com.example.finances.feature.transactions.di

import com.example.finances.core.di.ActivityScope
import com.example.finances.feature.transactions.data.TransactionsApi
import com.example.finances.feature.transactions.data.TransactionsRepoImpl
import com.example.finances.feature.transactions.domain.repository.TransactionsRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object TransactionsModule {
    @Provides
    @ActivityScope
    fun provideTransactionsApi(retrofit: Retrofit): TransactionsApi =
        retrofit.create(TransactionsApi::class.java)

    @Provides
    @ActivityScope
    fun provideTransactionsRepository(
        transactionsApi: TransactionsApi
    ): TransactionsRepository = TransactionsRepoImpl(transactionsApi)
} 