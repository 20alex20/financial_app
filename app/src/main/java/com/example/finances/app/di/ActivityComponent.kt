package com.example.finances.app.di

import android.app.Activity
import com.example.finances.app.di.modules.ActivityModule
import com.example.finances.app.di.modules.CategoriesModule
import com.example.finances.app.navigation.AppNavigationCoordinator
import com.example.finances.core.managers.NetworkConnectionObserver
import com.example.finances.feature.categories.di.common.CategoriesDependencies
import dagger.BindsInstance
import dagger.Component

@ActivityScope
@Component(
    dependencies = [AppComponent::class],
    modules = [
        ActivityModule::class,
        CategoriesModule::class
    ]
)
interface ActivityComponent : CategoriesDependencies {
    fun networkConnectionObserver(): NetworkConnectionObserver
    fun appNavigationCoordinator(): AppNavigationCoordinator

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance activity: Activity,
            appComponent: AppComponent
        ): ActivityComponent
    }
}
