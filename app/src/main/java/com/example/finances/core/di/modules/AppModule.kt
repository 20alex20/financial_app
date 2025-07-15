package com.example.finances.core.di.modules

import android.app.Application
import android.content.Context
import com.example.finances.core.di.ApplicationContext
import com.example.finances.core.utils.NetworkConnectionObserver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @ApplicationContext
    fun providesApplicationContext(app: Application): Context = app.applicationContext

    @Provides
    @Singleton
    fun providesNetworkConnectionObserver(@ApplicationContext context: Context): NetworkConnectionObserver {
        return NetworkConnectionObserver(context)
    }
}
