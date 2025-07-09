package com.example.finances.core.di.modules

import android.app.Activity
import android.content.Context
import com.example.finances.core.di.ActivityContext
import com.example.finances.core.di.ActivityScope
import com.example.finances.core.utils.NetworkConnectionObserver
import com.example.finances.core.utils.SplashScreenAnimator
import dagger.Binds
import dagger.Module

@Module
interface ActivityModule {
    @Binds
    @ActivityScope
    @ActivityContext
    fun bindsActivityContext(activity: Activity): Context

    @Binds
    @ActivityScope
    fun bindsNetworkConnectionObserver(): NetworkConnectionObserver

    @Binds
    @ActivityScope
    fun bindsSplashScreenAnimator(): SplashScreenAnimator
}
