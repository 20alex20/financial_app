package com.example.finances.app.di

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import com.example.finances.core.di.CoreComponent
import com.example.finances.app.di.modules.ActivityModule
import com.example.finances.app.di.modules.CategoriesModule
import com.example.finances.app.di.modules.CoreModule
import com.example.finances.app.navigation.AppNavigationCoordinator
import com.example.finances.core.di.common.CoreDependencies
import com.example.finances.core.utils.NetworkConnectionObserver
import com.example.finances.core.utils.usecases.ConvertAmountUseCase
import com.example.finances.feature.categories.di.common.CategoriesDependencies
import com.example.finances.feature.categories.navigation.CategoriesNavigation
import dagger.BindsInstance
import dagger.Component

@ActivityScope
@Component(
    dependencies = [AppComponent::class],
    modules = [
        ActivityModule::class,
        CoreModule::class,
        CategoriesModule::class
    ]
)
interface ActivityComponent : CoreDependencies, CategoriesDependencies {
    fun convertAmountUseCase(): ConvertAmountUseCase

    fun networkConnectionObserver(): NetworkConnectionObserver
    fun viewModelFactory(): ViewModelProvider.Factory
    fun appNavigationCoordinator(): AppNavigationCoordinator

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance activity: Activity,
            appComponent: AppComponent
        ): ActivityComponent
    }
}
