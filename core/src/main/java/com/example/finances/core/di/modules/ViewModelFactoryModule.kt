package com.example.finances.core.di.modules

import androidx.lifecycle.ViewModelProvider
import com.example.finances.core.di.CoreScope
import com.example.finances.core.utils.viewmodel.DaggerViewModelFactory
import dagger.Binds
import dagger.Module

@Module
interface ViewModelFactoryModule {
    @Binds
    @CoreScope
    fun bindsViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}
