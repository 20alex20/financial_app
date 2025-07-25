package com.example.finances.feature.settings.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Preferences

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class EncryptedPreferences
