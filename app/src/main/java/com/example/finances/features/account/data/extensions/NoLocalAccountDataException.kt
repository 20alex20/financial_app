package com.example.finances.features.account.data.extensions

class NoLocalAccountDataException(
    message: String = NO_LOCAL_DATABASE_ACCOUNT
) : Exception(message) {
    companion object {
        private const val NO_LOCAL_DATABASE_ACCOUNT = "Account not found in local database"
    }
}
