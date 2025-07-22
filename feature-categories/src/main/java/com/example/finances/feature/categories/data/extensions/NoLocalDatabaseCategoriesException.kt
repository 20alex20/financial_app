package com.example.finances.feature.categories.data.extensions

class NoLocalDatabaseCategoriesException(
    message: String = NO_LOCAL_DATABASE_ACCOUNT
) : Exception(message) {
    companion object {
        private const val NO_LOCAL_DATABASE_ACCOUNT = "Account not found in local database"
    }
}
