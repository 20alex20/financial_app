package com.example.finances.core.navigation.models

import androidx.compose.ui.graphics.painter.Painter

/**
 * data-класс для хранения информации о экранах, перечисляемых в NavigationBar
 */
data class NavBarItem(
    val title: String,
    val image: Painter,
    val route: String
)
