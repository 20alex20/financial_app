package com.example.finances.core.di.modules

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.finances.core.di.ActivityContext
import com.example.finances.core.di.ActivityScope
import com.example.finances.core.di.ApplicationContext
import com.example.finances.core.utils.NetworkConnectionObserver
import com.example.finances.core.utils.viewmodel.DaggerViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface ActivityModule {
    @Binds
    @ActivityScope
    fun bindsDaggerViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    companion object {
        @Provides
        @ActivityScope
        @ActivityContext
        fun providesActivityContext(activity: Context): Context = activity

        @Provides
        @ActivityScope
        fun providesNetworkConnectionObserver(@ApplicationContext context: Context): NetworkConnectionObserver {
            return NetworkConnectionObserver(context)
        }
    }
}
