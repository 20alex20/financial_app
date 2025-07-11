package com.example.finances.app.di.modules

import androidx.lifecycle.ViewModelProvider
import com.example.finances.app.di.ActivityScope
import com.example.finances.app.di.AppComponent
import com.example.finances.core.di.common.CoreComponentAdapter
import com.example.finances.core.di.common.CoreDependencies
import com.example.finances.core.utils.NetworkConnectionObserver
import com.example.finances.core.utils.usecases.ConvertAmountUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
interface CoreModule {
    @Binds
    fun bindsAppComponent(dependencies: AppComponent): CoreDependencies

    companion object {
        @Provides
        @ActivityScope
        fun providesNetworkConnectionObserver(
            dependencies: CoreDependencies
        ): NetworkConnectionObserver {
            return CoreComponentAdapter.networkConnectionObserver(dependencies)
        }

        @Provides
        fun providesRetrofit(): Retrofit {
            return CoreComponentAdapter.retrofit()
        }

        @Provides
        @ActivityScope
        fun providesConvertAmountUseCase(): ConvertAmountUseCase {
            return CoreComponentAdapter.convertAmountUseCase()
        }


        @Provides
        @ActivityScope
        fun providesViewModelFactory(): ViewModelProvider.Factory {
            return CoreComponentAdapter.viewModelFactory()
        }
    }
}
