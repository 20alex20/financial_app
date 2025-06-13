package com.example.financial_app.ui.theme

import androidx.compose.ui.graphics.Color

val LightGreen = Color(0xFFD4FAE6)
val Green = Color(0xFF2AE881)

val Red = Color(0xFFE46962)

val LightestGray = Color(0xFFF3EDF7)
val LightGray = Color(0xFFECE6F0)
val Gray = Color(0xFFCAC4D0)
val DarkGray = Color(0xFF79747E)
val DarkestGray = Color(0xFF49454F)

val RealWhite = Color(0xFFFFFFFF)
val White = Color(0xFFFEF7FF)
val Black = Color(0xFF1D1B20)

enum class TrailColor(val color: Color) {
    LIGHT_ARROW(Color(0x4D3C3C43)),
    DARK_ARROW(DarkGray),
    SWITCH_BACKGROUND(Color(0xFFE6E0E9)),
    SWITCH(DarkGray)
}
