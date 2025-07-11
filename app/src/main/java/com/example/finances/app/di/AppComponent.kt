package com.example.finances.app.di

import android.app.Application
import android.content.Context
import com.example.finances.app.di.modules.AppModule
import com.example.finances.core.di.common.ApplicationContext
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class]
)
interface AppComponent {
    @ApplicationContext fun applicationContext(): Context

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}
