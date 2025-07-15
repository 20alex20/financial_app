package com.example.finances.app

import android.app.Application
import android.content.Context
import androidx.work.*
import com.example.finances.core.data.sync.DataSyncWorker
import com.example.finances.core.data.sync.DataSyncWorkerFactory
import com.example.finances.core.di.AppComponent
import com.example.finances.core.di.DaggerAppComponent
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FinancesApplication : Application(), Configuration.Provider {
    lateinit var appComponent: AppComponent
        private set

    @Inject
    lateinit var workerFactory: DataSyncWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
        appComponent.inject(this)

        setupPeriodicSync()
    }

    private fun setupPeriodicSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<DataSyncWorker>(
            2, TimeUnit.HOURS, // Sync every 2 hours
            15, TimeUnit.MINUTES // Flex period of 15 minutes
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "data_sync",
                ExistingPeriodicWorkPolicy.KEEP,
                syncRequest
            )
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is FinancesApplication -> appComponent
        else -> (applicationContext as FinancesApplication).appComponent
    }
