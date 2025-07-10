package com.example.finances.core.di

import android.app.Application
import android.content.Context
import com.example.finances.core.di.modules.RetrofitClientModule
import com.example.finances.core.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        RetrofitClientModule::class
    ]
)
interface AppComponent {
    @ApplicationContext fun applicationContext(): Context
    fun retrofit(): Retrofit

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }
}
