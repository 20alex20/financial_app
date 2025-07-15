package com.example.finances.core.di

import android.app.Application
import android.content.Context
import com.example.finances.app.FinancesApplication
import com.example.finances.core.di.modules.RetrofitClientModule
import com.example.finances.core.di.modules.AppModule
import com.example.finances.core.di.modules.DatabaseModule
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        RetrofitClientModule::class,
        DatabaseModule::class
    ]
)
interface AppComponent {
    @ApplicationContext fun applicationContext(): Context
    fun retrofit(): Retrofit
    fun inject(application: FinancesApplication)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }
}
