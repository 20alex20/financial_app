package com.example.finances.core.di

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import com.example.finances.core.di.modules.ActivityModule
import com.example.finances.core.utils.NetworkConnectionObserver
import com.example.finances.core.utils.SplashScreenAnimator
import com.example.finances.features.account.di.AccountModule
import com.example.finances.features.categories.di.CategoriesModule
import com.example.finances.features.transactions.di.TransactionsModule
import dagger.BindsInstance
import dagger.Component

@ActivityScope
@Component(
    dependencies = [AppComponent::class],
    modules = [
        ActivityModule::class,
        AccountModule::class,
        CategoriesModule::class,
        TransactionsModule::class
    ]
)
interface ActivityComponent {
    fun networkConnectionObserver(): NetworkConnectionObserver

    fun splashScreenAnimator(): SplashScreenAnimator

    fun viewModelFactory(): ViewModelProvider.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance activity: Activity,
            appComponent: AppComponent
        ): ActivityComponent
    }
}
