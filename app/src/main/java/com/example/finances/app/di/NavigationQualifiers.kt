package com.example.finances.app.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AccountNavigation

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CategoriesNavigation

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TransactionsNavigation

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SettingsNavigation
