package com.example.finances.feature.transactions.api

import com.example.finances.feature.transactions.data.database.TransactionsDao

interface TransactionsDatabase {
    fun transactionsDao(): TransactionsDao
}
