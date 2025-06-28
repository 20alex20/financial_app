package com.example.finances.features.settings.domain.models

data class Setting(
    val name: String,
    val onClick: () -> Unit,
    val withSwitch: Boolean = false
)
