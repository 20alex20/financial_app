package com.example.finances.app.di.modules

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finances.app.di.ActivityScope
import com.example.finances.app.managers.DaggerViewModelFactory
import com.example.finances.core.di.ActivityContext
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.core.utils.viewmodel.ViewModelMapProvider
import com.example.finances.feature.account.domain.repository.ExternalTransactionsRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Provider

@Module
interface ActivityModule {
    @Binds
    @ActivityContext
    fun bindsActivityContext(activity: Activity): Context

    @Binds
    @ActivityScope
    fun bindsViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    companion object {
        @Provides
        fun providesViewModelProviders(
            providers: Set<@JvmSuppressWildcards ViewModelMapProvider>
        ): Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<BaseViewModel>> {
            return providers.flatMap { it.provide().entries }.associate { it.toPair() }
        }

        @Provides
        @ActivityScope
        fun providesExternalTransactionsRepo(): MutableStateFlow<ExternalTransactionsRepo?> {
            return MutableStateFlow(null)
        }
    }
}
