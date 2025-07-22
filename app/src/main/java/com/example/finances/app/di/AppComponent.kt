package com.example.finances.app.di

import android.app.Application
import com.example.finances.app.di.modules.AppModule
import com.example.finances.app.di.modules.CoreModule
import com.example.finances.core.api.CoreDependencies
import com.example.finances.core.utils.NetworkConnectionObserver
import com.example.finances.core.utils.usecases.ConvertAmountUseCase
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        CoreModule::class
    ]
)
interface AppComponent : CoreDependencies {
    fun retrofit(): Retrofit
    fun networkConnectionObserver(): NetworkConnectionObserver
    fun convertAmountUseCase(): ConvertAmountUseCase

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}
