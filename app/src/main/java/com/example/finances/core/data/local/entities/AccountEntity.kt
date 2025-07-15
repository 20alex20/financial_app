package com.example.finances.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.finances.core.utils.models.Currency
import com.example.finances.features.account.domain.models.Account
import com.example.finances.features.account.domain.models.ShortAccount

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val balance: Double,
    val currency: String
) {
    fun toAccount() = Account(
        id = id,
        name = name,
        balance = balance,
        currency = Currency.valueOf(currency)
    )

    fun toShortAccount() = ShortAccount(
        name = name,
        balance = balance,
        currency = Currency.valueOf(currency)
    )

    companion object {
        fun fromAccount(account: Account) = AccountEntity(
            id = account.id,
            name = account.name,
            balance = account.balance,
            currency = account.currency.name
        )

        fun fromShortAccount(shortAccount: ShortAccount, id: Int) = AccountEntity(
            id = id,
            name = shortAccount.name,
            balance = shortAccount.balance,
            currency = shortAccount.currency.name
        )
    }
} 