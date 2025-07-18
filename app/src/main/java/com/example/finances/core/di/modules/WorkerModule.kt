package com.example.finances.core.di.modules

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.finances.core.managers.DataSyncWorker
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class WorkerModule {
    @Provides
    @Singleton
    fun provideWorkerFactory(): WorkerFactory {
        return object : WorkerFactory() {
            override fun createWorker(
                appContext: Context,
                workerClassName: String,
                workerParameters: WorkerParameters
            ): ListenableWorker? {
                return when (workerClassName) {
                    DataSyncWorker::class.java.name -> {
                        DataSyncWorker(appContext, workerParameters)
                    }
                    else -> null
                }
            }
        }
    }
}
