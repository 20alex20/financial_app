package com.example.finances.core.di.modules

import android.content.Context
import com.example.finances.core.data.local.FinanceDatabase
import com.example.finances.core.data.sync.DataSyncWorkerFactory
import com.example.finances.core.di.ApplicationContext
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): FinanceDatabase {
        return FinanceDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun providesWorkerFactory(retrofit: Retrofit): DataSyncWorkerFactory {
        return DataSyncWorkerFactory(retrofit)
    }
} 