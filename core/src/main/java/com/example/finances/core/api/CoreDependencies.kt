package com.example.finances.core.api

import android.content.Context
import com.example.finances.core.di.ApplicationContext

interface CoreDependencies {
    @ApplicationContext
    fun applicationContext(): Context
}
