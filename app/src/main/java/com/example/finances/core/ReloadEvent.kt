package com.example.finances.core

sealed interface ReloadEvent {
    data object AccountUpdated : ReloadEvent
}
