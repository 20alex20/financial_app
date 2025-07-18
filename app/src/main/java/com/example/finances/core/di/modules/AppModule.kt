package com.example.finances.core.di.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.finances.core.managers.FinanceDatabase
import com.example.finances.core.di.ApplicationContext
import com.example.finances.core.managers.DataSync
import com.example.finances.core.managers.NetworkConnectionObserver
import com.example.finances.features.account.domain.repository.AccountRepo
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface AppModule {
    @Binds
    @ApplicationContext
    fun bindsApplicationContext(app: Application): Context

    companion object {
        @Provides
        @Singleton
        fun providesFinanceDatabase(@ApplicationContext context: Context): FinanceDatabase {
            return Room.databaseBuilder(context, FinanceDatabase::class.java, "finance_db").build()
        }

        @Provides
        @Singleton
        fun providesDataSyncManager(
            networkObserver: NetworkConnectionObserver,
            accountRepo: AccountRepo,
            transactionsRepo: TransactionsRepo,
            database: FinanceDatabase
        ): DataSync {
            return DataSync(networkObserver, accountRepo, transactionsRepo, database)
        }
    }
}
