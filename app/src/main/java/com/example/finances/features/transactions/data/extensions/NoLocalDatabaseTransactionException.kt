package com.example.finances.features.transactions.data.extensions

class NoLocalDatabaseTransactionException(
    message: String = NO_LOCAL_DATABASE_TRANSACTION
) : Exception(message) {
    companion object {
        private const val NO_LOCAL_DATABASE_TRANSACTION = "Transaction not found in local database"
    }
}
