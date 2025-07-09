package com.example.finances.core.di.modules

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.multibindings.Multibinds

@Module
abstract class ViewModelModule {
    @Multibinds
    abstract fun viewModelFactories(): Map<Class<out ViewModel>, @JvmSuppressWildcards ViewModel>
} 