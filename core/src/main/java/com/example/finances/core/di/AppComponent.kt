package com.example.finances.core.di

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import retrofit2.Retrofit
import javax.inject.Singleton

interface AppComponent {
    @ApplicationContext fun applicationContext(): Context
    fun retrofit(): Retrofit

    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }
} 