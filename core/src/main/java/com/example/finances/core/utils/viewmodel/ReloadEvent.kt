package com.example.finances.core.utils.viewmodel

sealed interface ReloadEvent {
    data object AccountUpdated : ReloadEvent
    data object TransactionCreatedUpdated : ReloadEvent
}
