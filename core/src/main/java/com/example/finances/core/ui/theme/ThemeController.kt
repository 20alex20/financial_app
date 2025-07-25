package com.example.finances.core.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

class ThemeController(originalDarkTheme: Boolean, originalPrimaryColor: Color) {
    var darkTheme by mutableStateOf(originalDarkTheme)
    var primaryColor by mutableStateOf(originalPrimaryColor)
}
