package com.example.finances.core.di

import android.app.Application
import com.example.finances.core.di.modules.RetrofitClientModule
import com.example.finances.core.di.modules.AppModule
import com.example.finances.core.managers.NetworkConnectionObserver
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
    fun networkConnectionObserver(): NetworkConnectionObserver

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }
}
