package com.example.finances.app.di

import android.app.Activity
import com.example.finances.core.di.ActivityComponent
import com.example.finances.core.di.ActivityScope
import com.example.finances.core.di.modules.ActivityModule
import com.example.finances.feature.account.di.AccountDependencies
import com.example.finances.feature.categories.di.CategoriesDependencies
import com.example.finances.feature.transactions.di.TransactionsDependencies
import com.example.finances.features.settings.ui.SettingsViewModel
import dagger.BindsInstance
import dagger.Component

@ActivityScope
@Component(
    dependencies = [AppComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent : 
    ActivityComponent,
    AccountDependencies,
    CategoriesDependencies,
    TransactionsDependencies {

    fun settingsViewModel(): SettingsViewModel

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance activity: Activity,
            appComponent: com.example.finances.core.di.AppComponent
        ): ActivityComponent
    }
} 