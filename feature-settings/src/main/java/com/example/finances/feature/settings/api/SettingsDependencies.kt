package com.example.finances.feature.settings.api

import android.content.Context
import com.example.finances.core.di.ActivityContext
import com.example.finances.core.managers.VibrateUseCase

interface SettingsDependencies {
    @ActivityContext
    fun activityContext(): Context

    fun vibrateUseCase(): VibrateUseCase
}
