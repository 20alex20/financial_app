package com.example.finances.app.di.modules

import com.example.finances.app.di.ActivityComponent
import com.example.finances.core.api.CoreAdapter
import com.example.finances.core.api.CoreComponentFactory
import com.example.finances.core.api.CoreDependencies
import com.example.finances.core.utils.NetworkConnectionObserver
import com.example.finances.core.utils.usecases.ConvertAmountUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
interface CoreModule {
    @Binds
    fun bindsAppComponent(dependencies: ActivityComponent): CoreDependencies

    companion object {
        @Provides
        @Singleton
        fun providesNetworkConnectionObserver(dependencies: CoreDependencies): CoreAdapter {
            return CoreComponentFactory.create(dependencies)
        }

        @Provides
        @Singleton
        fun providesRetrofit(coreAdapter: CoreAdapter): Retrofit {
            return coreAdapter.retrofit
        }

        @Provides
        @Singleton
        fun providesNetworkConnectionObserver(coreAdapter: CoreAdapter): NetworkConnectionObserver {
            return coreAdapter.networkConnectionObserver
        }

        @Provides
        @Singleton
        fun providesConvertAmountUseCase(coreAdapter: CoreAdapter): ConvertAmountUseCase {
            return coreAdapter.convertAmountUseCase
        }
    }
}
