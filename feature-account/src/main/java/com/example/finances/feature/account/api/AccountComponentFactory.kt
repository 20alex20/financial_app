package com.example.finances.feature.account.api

import com.example.finances.feature.account.di.AccountComponent

object AccountComponentFactory {
    fun create(dependencies: AccountDependencies): AccountFeature {
        return AccountComponent.create(dependencies).accountFeature()
    }
}
