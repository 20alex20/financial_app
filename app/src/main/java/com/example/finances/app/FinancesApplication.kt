package com.example.finances.app

import android.app.Application
import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.finances.core.di.AppComponent
import com.example.finances.core.di.DaggerAppComponent
import com.example.finances.core.managers.LogMessageWorker
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

class FinancesApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
        appComponent.networkConnectionObserver()
        
        scheduleLogMessageWorker()
    }
    
    private fun scheduleLogMessageWorker() {
        val targetTime = LocalDateTime.now()
            .withHour(22)
            .withMinute(40)
            .withSecond(0)
        
        val currentTime = LocalDateTime.now()
        val delay = targetTime.toEpochSecond(ZoneOffset.UTC) - currentTime.toEpochSecond(ZoneOffset.UTC)
        
        if (delay > 0) {
            val workRequest = OneTimeWorkRequestBuilder<LogMessageWorker>()
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
