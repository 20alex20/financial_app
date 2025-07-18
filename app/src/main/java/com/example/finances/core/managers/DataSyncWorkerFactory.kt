package com.example.finances.core.managers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSyncWorkerFactory @Inject constructor(
    private val database: FinanceDatabase,
    private val retrofit: Retrofit
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            DataSyncWorker::class.java.name -> DataSyncWorker(
                appContext,
                workerParameters,
                retrofit,
                database
            )
            else -> null
        }
    }
}
