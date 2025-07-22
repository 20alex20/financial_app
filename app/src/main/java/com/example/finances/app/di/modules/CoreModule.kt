package com.example.finances.app.di.modules

import com.example.finances.app.di.AppComponent
import com.example.finances.core.api.CoreAdapter
import com.example.finances.core.api.CoreComponentFactory
import com.example.finances.core.api.CoreDependencies
import com.example.finances.core.managers.NetworkConnectionObserver
import com.example.finances.core.managers.ConvertAmountUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
interface CoreModule {
    @Binds
    fun bindsCoreDependencies(dependencies: AppComponent): CoreDependencies

    companion object {
        @Provides
        @Singleton
        fun providesCoreAdapter(dependencies: CoreDependencies): CoreAdapter {
            return CoreComponentFactory.create(dependencies)
        }

        @Provides
        fun providesRetrofit(coreAdapter: CoreAdapter): Retrofit {
            return coreAdapter.retrofit
        }

        @Provides
        fun providesNetworkConnectionObserver(coreAdapter: CoreAdapter): NetworkConnectionObserver {
            return coreAdapter.networkConnectionObserver
        }

        @Provides
        fun providesConvertAmountUseCase(coreAdapter: CoreAdapter): ConvertAmountUseCase {
            return coreAdapter.convertAmountUseCase
        }
    }
}
