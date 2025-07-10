package com.example.finances.feature.account.di

import com.example.finances.core.di.ActivityScope
import com.example.finances.feature.account.data.AccountApi
import com.example.finances.feature.account.data.AccountRepoImpl
import com.example.finances.feature.account.domain.repository.AccountRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object AccountModule {
    @Provides
    @ActivityScope
    fun provideAccountApi(retrofit: Retrofit): AccountApi =
        retrofit.create(AccountApi::class.java)

    @Provides
    @ActivityScope
    fun provideAccountRepository(
        accountApi: AccountApi
    ): AccountRepository = AccountRepoImpl(accountApi)
} 