package com.example.finances.app

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.finances.core.di.AppComponent
import com.example.finances.core.di.DaggerAppComponent
import com.example.finances.core.managers.DataSyncWorker
import java.util.concurrent.TimeUnit

class FinancesApplication : Application(), Configuration.Provider {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
        appComponent.networkConnectionObserver()

        launchDataSyncWorker()
    }

    private fun launchDataSyncWorker() {
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<DataSyncWorker>(
                120, TimeUnit.MINUTES,
                2, TimeUnit.MINUTES
            ).build()
        )
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(appComponent.workerFactory())
            .build()

    companion object {
        private const val WORKER_NAME = "transaction_sync"
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is FinancesApplication -> appComponent
        else -> (applicationContext as FinancesApplication).appComponent
    }
