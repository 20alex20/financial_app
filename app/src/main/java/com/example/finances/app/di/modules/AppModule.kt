package com.example.finances.app.di.modules

import android.app.Application
import android.content.Context
import com.example.finances.core.di.ApplicationContext
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface AppModule {
    @Binds
    @Singleton
    @ApplicationContext
    fun bindsApplicationContext(app: Application): Context
}
