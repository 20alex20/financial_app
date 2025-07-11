package com.example.finances.core.di

import androidx.lifecycle.ViewModelProvider
import com.example.finances.core.di.common.CoreDependencies
import com.example.finances.core.di.modules.RetrofitClientModule
import com.example.finances.core.utils.NetworkConnectionObserver
import com.example.finances.core.utils.SplashScreenAnimator
import com.example.finances.core.utils.usecases.ConvertAmountUseCase
import dagger.Component
import retrofit2.Retrofit

/**
 * Core component that provides basic dependencies for the application.
 * This component is used as a dependency for feature-specific components.
 */
@CoreScope
@Component(
    dependencies = [CoreDependencies::class],
    modules = [
        RetrofitClientModule::class
    ]
)
interface CoreComponent {
    fun retrofit(): Retrofit
    fun networkConnectionObserver(): NetworkConnectionObserver
    fun splashScreenAnimator(): SplashScreenAnimator
    fun convertAmountUseCase(): ConvertAmountUseCase

    @Component.Factory
    interface Factory {
        fun create(dependencies: CoreDependencies): CoreComponent
    }

    companion object {
        fun create(dependencies: CoreDependencies): CoreComponent {
            return DaggerCoreComponent.factory().create(dependencies)
        }
    }
}
