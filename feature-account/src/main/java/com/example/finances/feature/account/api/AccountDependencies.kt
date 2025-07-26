package com.example.finances.feature.account.api

import com.example.finances.core.managers.ConvertAmountUseCase
import com.example.finances.core.managers.NetworkConnectionObserver
import retrofit2.Retrofit

interface AccountDependencies {
    fun retrofit(): Retrofit

    fun accountDatabase(): AccountDatabase

    fun networkConnectionObserver(): NetworkConnectionObserver

    fun convertAmountUseCase(): ConvertAmountUseCase
}
