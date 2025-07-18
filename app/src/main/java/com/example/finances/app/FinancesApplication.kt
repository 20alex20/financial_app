package com.example.finances.app

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.finances.core.di.AppComponent
import com.example.finances.core.di.DaggerAppComponent
import com.example.finances.core.managers.TransactionWorker
import java.time.LocalDateTime
import java.time.ZoneOffset
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
        val targetTime = LocalDateTime.now()
            .withHour(23)
            .withMinute(41)
            .withSecond(0)
        
        val currentTime = LocalDateTime.now()
        val delay = targetTime.toEpochSecond(ZoneOffset.UTC) - currentTime.toEpochSecond(ZoneOffset.UTC)
        
        if (delay > 0) {
            val workRequest = OneTimeWorkRequestBuilder<TransactionWorker>()
                .setInitialDelay(delay, TimeUnit.SECONDS)
                .build()
                
            WorkManager.getInstance(this).enqueue(workRequest)
        }
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is FinancesApplication -> appComponent
        else -> (applicationContext as FinancesApplication).appComponent
    }
