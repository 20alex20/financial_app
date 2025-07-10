package com.example.finances.feature.account.di

import com.example.finances.core.di.ActivityComponent
import retrofit2.Retrofit

interface AccountDependencies : ActivityComponent {
    fun retrofit(): Retrofit
} 