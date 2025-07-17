package com.example.finances.app

import android.app.Application
import android.content.Context
import com.example.finances.core.di.AppComponent
import com.example.finances.core.di.DaggerAppComponent

class FinancesApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
        appComponent.networkConnectionObserver()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is FinancesApplication -> appComponent
        else -> (applicationContext as FinancesApplication).appComponent
    }
