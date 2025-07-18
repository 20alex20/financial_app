package com.example.finances.app

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.finances.core.di.AppComponent
import com.example.finances.core.di.DaggerAppComponent
import com.example.finances.core.managers.TransactionWorker
import java.util.concurrent.TimeUnit

class FinancesApplication : Application(), Configuration.Provider {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
        appComponent.networkConnectionObserver()
        
        scheduleTransaction()
    }
    
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(appComponent.workerFactory())
            .build()
    
    private fun scheduleTransaction() {
        val workRequest = PeriodicWorkRequestBuilder<TransactionWorker>(
            20, TimeUnit.MINUTES,  // Repeat every 20 minutes
            5, TimeUnit.MINUTES    // Flex interval of 5 minutes
        ).build()
        
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "transaction_sync",                      // Unique work name
            ExistingPeriodicWorkPolicy.KEEP,        // Keep existing work if present
            workRequest
        )
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is FinancesApplication -> appComponent
        else -> (applicationContext as FinancesApplication).appComponent
    }
