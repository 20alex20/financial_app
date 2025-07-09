package com.example.finances.core.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.example.finances.core.di.modules.RetrofitClientModule
import com.example.finances.core.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        RetrofitClientModule::class
    ]
)
interface AppComponent {
    fun viewModelProviderFactory(): ViewModelProvider.Factory

    fun activityComponent(): ActivityComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }
}
