package com.example.finances.core.di

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import com.example.finances.core.di.modules.ActivityModule
import com.example.finances.core.utils.NetworkConnectionObserver
import com.example.finances.core.utils.SplashScreenAnimator
import dagger.BindsInstance

interface ActivityComponent {
    fun networkConnectionObserver(): NetworkConnectionObserver

    fun splashScreenAnimator(): SplashScreenAnimator

    fun viewModelFactory(): ViewModelProvider.Factory

    interface Factory {
        fun create(
            @BindsInstance activity: Activity,
            appComponent: AppComponent
        ): ActivityComponent
    }
} 