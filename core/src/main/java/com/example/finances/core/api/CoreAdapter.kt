package com.example.finances.core.api

import com.example.finances.core.utils.NetworkConnectionObserver
import com.example.finances.core.utils.usecases.ConvertAmountUseCase
import retrofit2.Retrofit
import javax.inject.Inject

class CoreAdapter @Inject constructor(
    val retrofit: Retrofit,
    val networkConnectionObserver: NetworkConnectionObserver,
    val convertAmountUseCase: ConvertAmountUseCase
)
