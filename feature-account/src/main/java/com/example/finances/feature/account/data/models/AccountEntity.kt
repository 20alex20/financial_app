package com.example.finances.feature.account.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val balance: Double,
    val currency: String,
    val isSynced: Boolean
)
