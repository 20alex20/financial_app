package com.example.financial_app.features.settings.domain.models

data class Setting(
    val name: String,
    val onClick: () -> Unit,
    val withSwitch: Boolean = false
)
