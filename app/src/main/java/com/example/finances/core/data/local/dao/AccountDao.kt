package com.example.finances.core.data.local.dao

import androidx.room.*
import com.example.finances.core.data.local.entities.AccountEntity

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts LIMIT 1")
    suspend fun getAccount(): AccountEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity)

    @Update
    suspend fun updateAccount(account: AccountEntity)
} 