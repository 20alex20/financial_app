package com.example.finances.core.di.modules

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.finances.core.di.ApplicationContext
import com.example.finances.core.utils.usecases.ConvertAmountUseCase
import com.example.finances.core.utils.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface AppModule {
    @Binds
    @Singleton
    @ApplicationContext
    fun bindsApplicationContext(app: Application): Context

    @Binds
    @Singleton
    fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @Singleton
    fun bindsConvertAmountUseCase(): ConvertAmountUseCase
}
