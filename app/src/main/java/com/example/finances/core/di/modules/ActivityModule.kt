package com.example.finances.core.di.modules

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.finances.core.di.ActivityContext
import com.example.finances.core.di.ActivityScope
import com.example.finances.core.utils.viewmodel.DaggerViewModelFactory
import dagger.Binds
import dagger.Module

@Module
interface ActivityModule {
    @Binds
    @ActivityContext
    fun bindsActivityContext(activity: Activity): Context

    @Binds
    @ActivityScope
    fun bindsViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}
