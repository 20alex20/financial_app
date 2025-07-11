package com.example.finances.app.di.modules

import android.app.Activity
import android.content.Context
import com.example.finances.app.di.ActivityScope
import com.example.finances.core.di.common.ActivityContext
import dagger.Binds
import dagger.Module

@Module
interface ActivityModule {
    @Binds
    @ActivityScope
    @ActivityContext
    fun bindsActivityContext(activity: Activity): Context
}
