package com.example.finances.app.di

import android.app.Application
import com.example.finances.core.di.AppComponent
import com.example.finances.core.di.modules.AppModule
import com.example.finances.core.di.modules.RetrofitClientModule
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
interface AppComponent : AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
} 