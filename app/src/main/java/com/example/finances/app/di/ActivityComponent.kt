package com.example.finances.app.di

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import com.example.finances.app.di.modules.AccountModule
import com.example.finances.app.di.modules.ActivityModule
import com.example.finances.app.di.modules.CategoriesModule
import com.example.finances.app.di.modules.TransactionsModule
import com.example.finances.app.managers.SplashScreenAnimator
import com.example.finances.app.navigation.AppNavigationCoordinator
import com.example.finances.feature.account.api.AccountDependencies
import com.example.finances.feature.categories.api.CategoriesDependencies
import com.example.finances.feature.transactions.api.TransactionsDependencies
import dagger.BindsInstance
import dagger.Component

@ActivityScope
@Component(
    dependencies = [AppComponent::class],
    modules = [
        ActivityModule::class,
        CategoriesModule::class,
        AccountModule::class,
        TransactionsModule::class
    ]
)
interface ActivityComponent
    : CategoriesDependencies, AccountDependencies, TransactionsDependencies {
    fun splashScreenAnimator(): SplashScreenAnimator
    fun viewModelFactory(): ViewModelProvider.Factory
    fun appNavigationCoordinator(): AppNavigationCoordinator

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance activity: Activity, appComponent: AppComponent): ActivityComponent
    }
}
