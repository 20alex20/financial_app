package com.example.finances.feature.account.api

import com.example.finances.feature.account.data.database.AccountDao

interface AccountDatabase {
    fun accountDao(): AccountDao
}
