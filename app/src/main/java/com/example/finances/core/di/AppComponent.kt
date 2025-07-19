package com.example.finances.core.di

import android.app.Application
import androidx.work.WorkerFactory
import com.example.finances.core.di.modules.RetrofitClientModule
import com.example.finances.core.di.modules.AppModule
import com.example.finances.core.di.modules.WorkerModule
import com.example.finances.core.managers.FinanceDatabase
import com.example.finances.core.managers.NetworkConnectionObserver
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        RetrofitClientModule::class,
        WorkerModule::class
    ]
)
interface AppComponent {
    fun networkConnectionObserver(): NetworkConnectionObserver
    fun retrofit(): Retrofit
    fun database(): FinanceDatabase
    fun workerFactory(): WorkerFactory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }
}
