package com.example.finances.core.di.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.finances.core.managers.FinanceDatabase
import com.example.finances.core.di.ApplicationContext
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
    }
}
