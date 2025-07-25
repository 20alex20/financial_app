package com.example.finances.feature.settings.domain.models

enum class VibrationDuration(val duration: Long) {
    DISABLED(0),
    SHORT(50),
    MEDIUM(100),
    LONG(200)
}
