package com.example.finances.core.utils.viewmodel

/**
 * Sealed class contains objects corresponding to event types
 * that require screen content reloading
 */
sealed interface ReloadEvent {
    data object AccountUpdated : ReloadEvent
    data object TransactionCreatedUpdated : ReloadEvent
}
