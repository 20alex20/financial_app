package com.example.finances.features.navigation.domain.models

import androidx.compose.ui.graphics.painter.Painter

/**
 * data-класс для хранения информации о экранах, перечисляемых в NavigationBar
 */
data class BarItem(
    val title: String,
    val image: Painter,
    val route: String
)
