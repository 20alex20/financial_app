package com.example.finances.core.di.common

import android.app.Activity
import android.content.Context

interface CoreDependencies {
    @ApplicationContext fun applicationContext(): Context

    @ActivityContext fun activityContext(): Context

    fun activity(): Activity
}
