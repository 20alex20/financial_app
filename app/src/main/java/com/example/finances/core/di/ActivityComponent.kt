package com.example.finances.core.di

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import com.example.finances.core.di.modules.ActivityModule
import com.example.finances.core.managers.DataSyncOnConnection
import com.example.finances.core.managers.SplashScreenAnimator
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
    fun splashScreenAnimator(): SplashScreenAnimator
    fun viewModelFactory(): ViewModelProvider.Factory
    fun dataSyncOnConnection(): DataSyncOnConnection

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance activity: Activity,
            appComponent: AppComponent
        ): ActivityComponent
    }
}
